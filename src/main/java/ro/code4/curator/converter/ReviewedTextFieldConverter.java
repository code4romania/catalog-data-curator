package ro.code4.curator.converter;

import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ReviewedTextFinding;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;

@Component
public class ReviewedTextFieldConverter {

	public ReviewedTextFinding toEntity(ParsedTextFindingTO to) {
		ReviewedTextFinding result = new ReviewedTextFinding();

		result.setFieldName(to.getFieldName());
		result.setParsedValue(to.getParsedValue());
		result.setStartPos(to.getStartPos());
		result.setEndPos(to.getEndPos());
		result.setParserId(to.getParserId());

		return result;
	}

	public ParsedTextFindingTO toTO(ReviewedTextFinding entity) {
		ParsedTextFindingTO to = new ParsedTextFindingTO();

		to.setFieldName(entity.getFieldName());
		to.setParsedValue(entity.getParsedValue());
		to.setStartPos(entity.getStartPos());
		to.setEndPos(entity.getEndPos());
		to.setParserId(entity.getParserId());

		return to;
	}
}
