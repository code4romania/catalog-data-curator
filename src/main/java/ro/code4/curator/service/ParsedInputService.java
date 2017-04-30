package ro.code4.curator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.code4.curator.converter.ParsedInputFieldConverter;
import ro.code4.curator.converter.ParsedInputConverter;
import ro.code4.curator.entity.ParsedInput;
import ro.code4.curator.entity.ParsedInputField;
import ro.code4.curator.repository.ParsedInputRepository;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;
import ro.code4.curator.transferObjects.ParsedInputTO;

@Service
public class ParsedInputService {

    @Autowired
    private ParsedInputRepository parsedInputRepo;

    @Autowired
    private ParsedInputFieldConverter fieldConverter;

    @Autowired
    private ParsedInputConverter inputConverter;

    public List<ParsedInputTO> list() {
        List<ParsedInputTO> result = new ArrayList<>();

        Iterable<ParsedInput> all = parsedInputRepo.findAll();
        for (ParsedInput entity : all)
            result.add(inputConverter.toTo(entity));

        return result;
    }

    public ParsedInputTO getById(int id) {
        ParsedInput parsedInput = parsedInputRepo.findOne(id);
        if (parsedInput == null)
            return null;
        return inputConverter.toTo(parsedInput);
    }

    public ParsedInputTO acceptTextParsing(ParsedInputTO newEntry) {
        ParsedInput duplicate = findDuplicate(newEntry);
        if (isDuplicateFound(duplicate)) {
            validateFullTextMatches(newEntry, duplicate);

            // for each field, look for matching interpretations
            increaseVotesForMatchingFields(newEntry, duplicate);

            // save all updates to the existing parsed input entry; cascades
            ParsedInput persisted = persist(duplicate);
            return inputConverter.toTo(persisted);
        }

        // for each field, create an interpretation with 1 vote
        ParsedInput parsedInput = inputConverter.toEntity(newEntry);
        for (ParsedInputField field : parsedInput.getParsedFields()) {
            field.setVotes(1);
        }

        // save all; cascades on fields
        ParsedInput persisted = persist(parsedInput);
        return inputConverter.toTo(persisted);
    }

    private boolean isDuplicateFound(ParsedInput duplicate) {
        return duplicate != null;
    }

    private ParsedInput persist(ParsedInput duplicate) {
        return parsedInputRepo.save(duplicate);
    }

    private void increaseVotesForMatchingFields(ParsedInputTO newEntry, ParsedInput duplicate) {
        for (ParsedInputFieldTO newField : newEntry.getParsedFields()) {
            boolean matched = false;

            for (ParsedInputField existingField : duplicate.getParsedFields()) {
                if (existingField.isFieldContentAndPositionMatch(newField)) {
                    // matched; increase votes count
                    existingField.incrVotes();
                    matched = true;
                }
            }

            if (!matched) {
                // if not matched, create another field entry on the same parsed input
                ParsedInputField field = fieldConverter.toEntity(newField);
                duplicate.getParsedFields().add(field);
            }
        }
    }

    private ParsedInput findDuplicate(ParsedInputTO input) {
        List<ParsedInput> existing = parsedInputRepo.findByTextTypeAndTextSourceId(
                input.getTextType(), input.getTextSourceId());
        validateMatchCount(input, existing);

        if (existing == null || existing.isEmpty())
            return null;

        return existing.get(0);
    }

    private void validateMatchCount(ParsedInputTO input, List<ParsedInput> existing) {
        if (existing.size() > 1) {
            // more than one match, this is an anomaly
            throw new IllegalStateException("More than one parse input found for " +
                    "type=|" + input.getTextType()
                    + "| and sourceId=|" + input.getTextSourceId()
                    + "|. This is an anomaly, that needs to be corrected before submitting new parse results");
        }
    }

    private void validateFullTextMatches(ParsedInputTO parsedInputTO, ParsedInput existingParsedInput) {
        if (!existingParsedInput.hasEqualFullText(parsedInputTO))
            throw fullTextMismatchException(parsedInputTO, existingParsedInput);
    }

    private IllegalArgumentException fullTextMismatchException(ParsedInputTO newInput,
                                                               ParsedInput existingInput) {
        return new IllegalArgumentException(
                "The full text does not match for type=|" + newInput.getTextType() +
                        "| and sourceId=|" + newInput.getTextSourceId()
                        + "|. The new input cannot be accepted because start " +
                        "and end indices will not match.\n" +
                        "Existing full text: "
                        + existingInput.getFullText().trim() + "\n" +
                        "New input full text: " + newInput.getFullText().trim());
    }

    public void deleteById(int id) {
        parsedInputRepo.delete(id);
    }
}
