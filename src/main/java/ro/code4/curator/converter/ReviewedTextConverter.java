package ro.code4.curator.converter;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ReviewedText;
import ro.code4.curator.entity.ReviewedFinding;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;
import ro.code4.curator.transferObjects.ParsedTextTO;
import ro.code4.curator.transferObjects.ShallowReviewedTextTO;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReviewedTextConverter {

	private static Logger LOGGER = Logger.getLogger(ReviewedTextConverter.class);

	public ReviewedText toEntity(ParsedTextTO to) {
		ReviewedText result = new ReviewedText();

		// set own properties
		result.setId(to.getEntityId());
        result.setTextType(to.getTextType());
        result.setTextSourceId(to.getTextSourceId());

		// iterate parsed input fields
		Map<ParsedTextFindingTO, ReviewedFinding> fieldsMapping = new HashMap<>();
		ReviewedTextFieldConverter fieldConverter = new ReviewedTextFieldConverter();
		for (ParsedTextFindingTO fieldTO : to.getParsedFields()) {
			ReviewedFinding convertedField = fieldConverter.toEntity(fieldTO);
			convertedField.setReviewedInputId(result);
			result.getReviewedFields().add(convertedField);
			fieldsMapping.put(fieldTO, convertedField);
		}

		// check if fields have parent fields that need to be matched
		for (ParsedTextFindingTO fieldTO : to.getParsedFields()) {
			if (fieldTO.getParentFieldName() != null && !fieldTO.getParentFieldName().isEmpty()) {
				// find the parent field
				boolean found = false;
				for (ReviewedFinding parentCandidate : result.getReviewedFields())
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

	public ParsedTextTO toTO(ReviewedText entity) {
		ParsedTextTO result = new ParsedTextTO();

		// set own properties
		result.setEntityId(entity.getId());
		result.setTextType(entity.getTextType());
		result.setTextSourceId(entity.getTextSourceId());

		// set parsed input fields
		ReviewedTextFieldConverter fieldConverter = new ReviewedTextFieldConverter();
		for (ReviewedFinding field : entity.getReviewedFields()) {
			ParsedTextFindingTO fieldTO = fieldConverter.toTO(field);
			result.getParsedFields().add(fieldTO);
		}

		return result;
	}

	public ShallowReviewedTextTO toShallowTO(ReviewedText entity) {
		ShallowReviewedTextTO result = new ShallowReviewedTextTO();

		result.setId(entity.getId());
		result.setTextSourceId(entity.getTextSourceId());
		result.setTextType(entity.getTextType());

		return result;
	}
}
