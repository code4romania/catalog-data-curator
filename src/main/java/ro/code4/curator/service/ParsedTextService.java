package ro.code4.curator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.code4.curator.converter.ParsedTextConverter;
import ro.code4.curator.converter.ParsedTextFieldConverter;
import ro.code4.curator.entity.ParsedText;
import ro.code4.curator.entity.TextFinding;
import ro.code4.curator.entity.ParsedTextManager;
import ro.code4.curator.repository.ParsedTextRepository;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;
import ro.code4.curator.transferObjects.ParsedTextTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParsedTextService implements ParsedTextManager {

    @Autowired
    private ParsedTextRepository parsedInputRepo;

    @Autowired
    private ParsedTextFieldConverter fieldConverter = new ParsedTextFieldConverter();

    @Autowired
    private ParsedTextConverter inputConverter = new ParsedTextConverter();

    @Override
    public List<ParsedTextTO> getAllParsedTexts() {
        List<ParsedTextTO> result = new ArrayList<>();

        Iterable<ParsedText> all = parsedInputRepo.findAll();
        for (ParsedText entity : all)
            result.add(inputConverter.toTo(entity));

        return result;
    }

    @Override
    public ParsedTextTO getParsedTextById(int id) {
        ParsedText parsedInput = parsedInputRepo.findOne(id);
        if (parsedInput == null)
            return null;
        return inputConverter.toTo(parsedInput);
    }

    @Override
    public ParsedTextTO submitParsedText(ParsedTextTO newEntry) {
        ParsedText duplicate = findDuplicate(newEntry);
        if (isDuplicateFound(duplicate)) {
            validateFullTextMatches(newEntry, duplicate);

            // for each field, look for matching interpretations
            increaseVotesForMatchingFields(newEntry, duplicate);

            // save all updates to the existing parsed input entry; cascades
            ParsedText persisted = persist(duplicate);
            return inputConverter.toTo(persisted);
        }

        // for each field, create an interpretation with 1 vote
        ParsedText parsedInput = inputConverter.toEntity(newEntry);
        for (TextFinding field : parsedInput.getParsedFields()) {
            field.setVotes(1);
        }

        // save all; cascades on fields
        ParsedText persisted = persist(parsedInput);
        return inputConverter.toTo(persisted);
    }

    @Override
    public void deleteParsedTextById(int id) {
        parsedInputRepo.delete(id);
    }

    private boolean isDuplicateFound(ParsedText duplicate) {
        return duplicate != null;
    }

    private ParsedText persist(ParsedText duplicate) {
        return parsedInputRepo.save(duplicate);
    }

    private void increaseVotesForMatchingFields(ParsedTextTO newEntry, ParsedText duplicate) {
        for (ParsedTextFindingTO newField : newEntry.getParsedFields()) {
            boolean matched = false;

            for (TextFinding existingFinding : duplicate.getParsedFields()) {
                if (existingFinding.isFieldContentAndPositionMatch(newField)) {
                    // matched; increase votes count
                    existingFinding.incrVotes();
                    matched = true;
                }
            }

            if (!matched) {
                // if not matched, create another field entry on the same parsed input
                TextFinding field = fieldConverter.toEntity(newField);
                duplicate.getParsedFields().add(field);
            }
        }
    }

    private ParsedText findDuplicate(ParsedTextTO input) {
        List<ParsedText> existing = parsedInputRepo.findByTextTypeAndTextSourceId(
                input.getTextType(), input.getTextSourceId());
        validateMatchCount(input, existing);

        if (existing == null || existing.isEmpty())
            return null;

        return existing.get(0);
    }

    private void validateMatchCount(ParsedTextTO input, List<ParsedText> existing) {
        if (existing.size() > 1) {
            // more than one match, this is an anomaly
            throw new IllegalStateException("More than one parse input found for " +
                    "type=|" + input.getTextType()
                    + "| and sourceId=|" + input.getTextSourceId()
                    + "|. This is an anomaly, that needs to be corrected before submitting new parse results");
        }
    }

    private void validateFullTextMatches(ParsedTextTO parsedInputTO, ParsedText existingParsedInput) {
        if (!existingParsedInput.hasEqualFullText(parsedInputTO))
            throw fullTextMismatchException(parsedInputTO, existingParsedInput);
    }

    private IllegalArgumentException fullTextMismatchException(ParsedTextTO newInput,
                                                               ParsedText existingInput) {
        return new IllegalArgumentException(
                "The full text does not match for type=|" + newInput.getTextType() +
                        "| and sourceId=|" + newInput.getTextSourceId()
                        + "|. The new input cannot be accepted because start " +
                        "and end indices will not match.\n" +
                        "Existing full text: "
                        + existingInput.getFullText().trim() + "\n" +
                        "New input full text: " + newInput.getFullText().trim());
    }

    public void setParsedInputRepo(ParsedTextRepository parsedInputRepo) {
        this.parsedInputRepo = parsedInputRepo;
    }
}
