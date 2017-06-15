package ro.code4.curator.converter;

import org.springframework.stereotype.Component;
import ro.code4.curator.entity.TextFinding;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;

@Component
public class ParsedTextFieldConverter {

    public TextFinding toEntity(ParsedTextFindingTO to) {
		TextFinding result = new TextFinding();

		result.setFieldName(to.getFieldName());
		result.setParsedValue(to.getParsedValue());
		result.setStartPos(to.getStartPos());
		result.setEndPos(to.getEndPos());
		result.setParserId(to.getParserId());
		result.setVotes(to.getVotes());

		return result;
	}

	public ParsedTextFindingTO toTo(TextFinding entity) {
		ParsedTextFindingTO to = new ParsedTextFindingTO();

		to.setFieldName(entity.getFieldName());
		to.setParsedValue(entity.getParsedValue());
		to.setStartPos(entity.getStartPos());
		to.setEndPos(entity.getEndPos());
		to.setParserId(entity.getParserId());
		to.setVotes(entity.getVotes());

		return to;
	}
}
