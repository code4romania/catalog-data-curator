package ro.code4.curator.converter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ParsedText;
import ro.code4.curator.entity.TextFinding;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;
import ro.code4.curator.transferObjects.ParsedTextTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParsedTextConverter {

	private static Logger LOGGER = Logger.getLogger(ParsedTextConverter.class);

	@Autowired
	private ParsedTextFieldConverter converter = new ParsedTextFieldConverter();

    public ParsedTextConverter() {
    }

    public ParsedText toEntity(ParsedTextTO to) {
		ParsedText result = new ParsedText();

		// set own properties
		result.setId(to.getEntityId());
        result.setText(to.getText());
        result.setReviewed(to.isReviewed());

		// iterate parsed input fields
		Map<ParsedTextFindingTO, TextFinding> fieldsMapping = new HashMap<>();
		for (ParsedTextFindingTO fieldTO : to.getParsedFields()) {
			TextFinding convertedField = converter.toEntity(fieldTO);
			convertedField.setParsedInputId(result);
			result.getParsedFields().add(convertedField);
			fieldsMapping.put(fieldTO, convertedField);
		}

		// check if fields have parent fields that need to be matched
		for (ParsedTextFindingTO fieldTO : to.getParsedFields()) {
			if (fieldTO.getParentFieldName() != null && !fieldTO.getParentFieldName().isEmpty()) {
				// find the parent field
				boolean found = false;
				for (TextFinding parentCandidate : result.getParsedFields())
					if (fieldTO.getParentFieldName().equals(parentCandidate.getFieldName())) {
						fieldsMapping.get(fieldTO).setParentField(parentCandidate);
						found = true;
						break;
					}

				if (!found)
					LOGGER.error("Could not find parent field |" + fieldTO.getParentFieldName() + "| for field |" + fieldTO.getFieldName()
							+ "|");
			}
		}

		return result;
	}

	public ParsedTextTO toTo(ParsedText entity) {
		ParsedTextTO result = new ParsedTextTO();

		// set own properties
		result.setEntityId(entity.getId());
        result.setText(entity.getText());
		result.setReviewed(entity.isReviewed());

		// set parsed input fields
		List<TextFinding> all = entity.getParsedFields();
		for (TextFinding field : all) {
			ParsedTextFindingTO fieldTO = converter.toTo(field);
			result.getParsedFields().add(fieldTO);
		}

		return result;
	}
}
