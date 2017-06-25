package ro.code4.curator.converter;

import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ReviewedFinding;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;

@Component
public class ReviewedTextFieldConverter {

	public ReviewedFinding toEntity(ParsedTextFindingTO to) {
		ReviewedFinding result = new ReviewedFinding();

		result.setFieldName(to.getFieldName());
		result.setParsedValue(to.getParsedValue());
		result.setStartPos(to.getStartPos());
		result.setEndPos(to.getEndPos());
		result.setParserId(to.getParserId());

		return result;
	}

	public ParsedTextFindingTO toTO(ReviewedFinding entity) {
		ParsedTextFindingTO to = new ParsedTextFindingTO();

		to.setFieldName(entity.getFieldName());
		to.setParsedValue(entity.getParsedValue());
		to.setStartPos(entity.getStartPos());
		to.setEndPos(entity.getEndPos());
		to.setParserId(entity.getParserId());

		return to;
	}
}
