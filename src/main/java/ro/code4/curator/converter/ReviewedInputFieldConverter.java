package ro.code4.curator.converter;

import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ReviewedInputField;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;

@Component
public class ReviewedInputFieldConverter {

	public ReviewedInputField toEntity(ParsedInputFieldTO to) {
		ReviewedInputField result = new ReviewedInputField();

		result.setFieldName(to.getFieldName());
		result.setParsedValue(to.getParsedValue());
		result.setStartPos(to.getStartPos());
		result.setEndPos(to.getEndPos());
		result.setParserId(to.getParserId());

		return result;
	}

	public ParsedInputFieldTO toTO(ReviewedInputField entity) {
		ParsedInputFieldTO to = new ParsedInputFieldTO();

		to.setFieldName(entity.getFieldName());
		to.setParsedValue(entity.getParsedValue());
		to.setStartPos(entity.getStartPos());
		to.setEndPos(entity.getEndPos());
		to.setParserId(entity.getParserId());

		return to;
	}
}
