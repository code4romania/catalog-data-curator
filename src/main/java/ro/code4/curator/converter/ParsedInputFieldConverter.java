package ro.code4.curator.converter;

import org.springframework.stereotype.Component;
import ro.code4.curator.entity.ParsedInputField;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;

@Component
public class ParsedInputFieldConverter {

	public ParsedInputField convertTOtoEntity(ParsedInputFieldTO to) {
		ParsedInputField result = new ParsedInputField();

		result.setFieldName(to.getFieldName());
		result.setParsedValue(to.getParsedValue());
		result.setStartPos(to.getStartPos());
		result.setEndPos(to.getEndPos());
		result.setParserId(to.getParserId());

		return result;
	}

	public ParsedInputFieldTO convertEntityToTO(ParsedInputField entity) {
		ParsedInputFieldTO to = new ParsedInputFieldTO();

		to.setFieldName(entity.getFieldName());
		to.setParsedValue(entity.getParsedValue());
		to.setStartPos(entity.getStartPos());
		to.setEndPos(entity.getEndPos());
		to.setParserId(entity.getParserId());

		return to;
	}
}
