package ro.code4.curator.converter;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ReviewedInput;
import ro.code4.curator.transferObjects.ParsedInputTO;
import ro.code4.curator.transferObjects.ShallowReviewedInputTO;
import ro.code4.curator.entity.ReviewedInputField;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;

@Component
public class ReviewedInputConverter {

	private static Logger LOGGER = Logger.getLogger(ReviewedInputConverter.class);

	public ReviewedInput toEntity(ParsedInputTO to) {
		ReviewedInput result = new ReviewedInput();

		// set own properties
		result.setId(to.getEntityId());
		result.setTextSourceId(to.getTextSourceId());
		result.setTextType(to.getTextType());
		result.setFullText(to.getFullText());

		// iterate parsed input fields
		Map<ParsedInputFieldTO, ReviewedInputField> fieldsMapping = new HashMap<>();
		ReviewedInputFieldConverter fieldConverter = new ReviewedInputFieldConverter();
		for (ParsedInputFieldTO fieldTO : to.getParsedFields()) {
			ReviewedInputField convertedField = fieldConverter.toEntity(fieldTO);
			convertedField.setReviewedInputId(result);
			result.getReviewedFields().add(convertedField);
			fieldsMapping.put(fieldTO, convertedField);
		}

		// check if fields have parent fields that need to be matched
		for (ParsedInputFieldTO fieldTO : to.getParsedFields()) {
			if (fieldTO.getParentFieldName() != null && !fieldTO.getParentFieldName().isEmpty()) {
				// find the parent field
				boolean found = false;
				for (ReviewedInputField parentCandidate : result.getReviewedFields())
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

	public ParsedInputTO toTO(ReviewedInput entity) {
		ParsedInputTO result = new ParsedInputTO();

		// set own properties
		result.setEntityId(entity.getId());
		result.setFullText(entity.getFullText());
		result.setTextSourceId(entity.getTextSourceId());
		result.setTextType(entity.getTextType());

		// set parsed input fields
		ReviewedInputFieldConverter fieldConverter = new ReviewedInputFieldConverter();
		for (ReviewedInputField field : entity.getReviewedFields()) {
			ParsedInputFieldTO fieldTO = fieldConverter.toTO(field);
			result.getParsedFields().add(fieldTO);
		}

		return result;
	}

	public ShallowReviewedInputTO toShallowTO(ReviewedInput entity) {
		ShallowReviewedInputTO result = new ShallowReviewedInputTO();

		result.setId(entity.getId());
		result.setTextSourceId(entity.getTextSourceId());
		result.setTextType(entity.getTextType());

		return result;
	}
}
