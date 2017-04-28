package ro.code4.curator.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import ro.code4.curator.transferObjects.ParsedInputTO;
import ro.code4.curator.entity.ParsedInput;
import ro.code4.curator.entity.ParsedInputField;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;

@Component
public class ParsedInputConverter {

	private static Logger LOGGER = Logger.getLogger(ParsedInputConverter.class);

	public ParsedInput convertTOtoEntity(ParsedInputTO to) {
		ParsedInput result = new ParsedInput();

		// set own properties
		result.setId(to.getEntityId());
		result.setTextSourceId(to.getTextSourceId());
		result.setTextType(to.getTextType());
		result.setFullText(to.getFullText());

		// iterate parsed input fields
		Map<ParsedInputFieldTO, ParsedInputField> fieldsMapping = new HashMap<>();
		ParsedInputFieldConverter fieldConverter = new ParsedInputFieldConverter();
		for (ParsedInputFieldTO fieldTO : to.getParsedFields()) {
			ParsedInputField convertedField = fieldConverter.convertTOtoEntity(fieldTO);
			convertedField.setParsedInputParent(result);
			result.getParsedFields().add(convertedField);
			fieldsMapping.put(fieldTO, convertedField);
		}

		// check if fields have parent fields that need to be matched
		for (ParsedInputFieldTO fieldTO : to.getParsedFields()) {
			if (fieldTO.getParentFieldName() != null && !fieldTO.getParentFieldName().isEmpty()) {
				// find the parent field
				boolean found = false;
				for (ParsedInputField parentCandidate : result.getParsedFields())
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

	public ParsedInputTO convertEntityToTO(ParsedInput entity) {
		ParsedInputTO result = new ParsedInputTO();

		// set own properties
		result.setEntityId(entity.getId());
		result.setFullText(entity.getFullText());
		result.setTextSourceId(entity.getTextSourceId());
		result.setTextType(entity.getTextType());

		// set parsed input fields
		ParsedInputFieldConverter fieldConverter = new ParsedInputFieldConverter();
		for (ParsedInputField field : entity.getParsedFields()) {
			ParsedInputFieldTO fieldTO = fieldConverter.convertEntityToTO(field);
			result.getParsedFields().add(fieldTO);
		}

		return result;
	}
}
