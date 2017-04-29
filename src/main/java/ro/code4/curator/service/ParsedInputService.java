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


    public ParsedInputTO acceptTextParsing(ParsedInputTO parsedInputTO) {
        String textType = parsedInputTO.getTextType();
        String textSourceId = parsedInputTO.getTextSourceId();

        List<ParsedInput> others = parsedInputRepo.findByTextTypeAndTextSourceId(textType, textSourceId);

        // check if there is already a parse input for the same type and source ID
        if (others.size() == 1) {
            ParsedInput existingParsedInput = others.get(0);

            // check if the full text is matching
            if (!existingParsedInput.getFullText().trim().equals(parsedInputTO.getFullText().trim()))
                throw new IllegalArgumentException("The full text does not match for type=|" + textType + "| and sourceId=|" + textSourceId
                        + "|. The new input cannot be accepted because start and end indices will not match.\nExisting full text: "
                        + existingParsedInput.getFullText().trim() + "\nNew input full text: " + parsedInputTO.getFullText().trim());

            // for each field, look for matching interpretations
            for (ParsedInputFieldTO fieldTO : parsedInputTO.getParsedFields()) {
                boolean matched = false;

                for (ParsedInputField existingField : existingParsedInput.getParsedFields()) {
                    if (fieldTO.getFieldName().equals(existingField.getFieldName())) {
                        // match if the value, start pos and end pos are the same
                        String newValue = fieldTO.getParsedValue();
                        int newStartPos = fieldTO.getStartPos();
                        int newEndPos = fieldTO.getEndPos();
                        String value = existingField.getParsedValue();
                        int startPos = existingField.getStartPos();
                        int endPos = existingField.getEndPos();

                        if (newValue.equalsIgnoreCase(value) && newStartPos == startPos && newEndPos == endPos) {
                            // matched; increase votes count
                            existingField.setVotes(existingField.getVotes() + 1);
                            matched = true;
                        }
                    }
                }

                if (!matched) {
                    // if not matched, create another field entry on the same parsed input
                    ParsedInputField field = fieldConverter.convertTOtoEntity(fieldTO);
                    existingParsedInput.getParsedFields().add(field);
                }
            }

            // save all updates to the existing parsed input entry; cascades
            return inputConverter.convertEntityToTO(parsedInputRepo.save(existingParsedInput));
        } else if (others.size() > 1) {
            // more than one match, this is an anomaly
            throw new IllegalStateException("More than one parse input found for type=|" + textType + "| and sourceId=|" + textSourceId
                    + "|. This is an anomaly, that needss to be corrected before submitting new parse results");
        } else { // no match
            ParsedInputConverter converter = new ParsedInputConverter();

            // for each field, create an interpretation with 1 vote
            ParsedInput parsedInput = converter.convertTOtoEntity(parsedInputTO);
            for (ParsedInputField field : parsedInput.getParsedFields()) {
                field.setVotes(1);
            }

            // save all; cascades on fields
            return inputConverter.convertEntityToTO(parsedInputRepo.save(parsedInput));
        }
    }

    public List<ParsedInputTO> list() {
        List<ParsedInputTO> result = new ArrayList<>();

        for (ParsedInput entity : parsedInputRepo.findAll())
            result.add(inputConverter.convertEntityToTO(entity));

        return result;
    }

    public ParsedInputTO getById(int id) {
        ParsedInput parsedInput = parsedInputRepo.findOne(id);
        if (parsedInput == null)
            return null;
        return inputConverter.convertEntityToTO(parsedInput);
    }

    public void deleteById(int id) {
        parsedInputRepo.delete(id);
    }
}
