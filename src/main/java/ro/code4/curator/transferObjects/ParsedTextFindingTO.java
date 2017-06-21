package ro.code4.curator.transferObjects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.code4.curator.entity.TextFinding;

@Data
@NoArgsConstructor
public class ParsedTextFindingTO {
	@JsonBackReference(value = "parsedInputId")
	private ParsedTextTO parsedInputId;
	@JsonBackReference(value = "parentField")
	private TextFinding parentField;

	private String fieldName;
	private int startPos;
	private int endPos;
	private String parsedValue;
	private String parserId;
	private String parentFieldName;
    private double votes;

    public static ParsedTextFindingTO with(ParsedTextTO parsedText,
                                           String parsedValue, String fieldName, int startPos, String parserId) {
        ParsedTextFindingTO to = new ParsedTextFindingTO();
        to.setParsedInputId(parsedText);
        to.setFieldName(fieldName);
        to.setStartPos(startPos);
        to.setEndPos(parsedValue.length()+startPos);
        to.setParsedValue(parsedValue);
        to.setParserId(parserId);
        return to;
    }
}
