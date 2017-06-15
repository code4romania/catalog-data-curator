package ro.code4.curator.transferObjects;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParsedTextTO {
	private int entityId;

	private String fullText;
	private String textType;
	private String textSourceId;
	private boolean reviewed;

	private List<ParsedTextFindingTO> parsedFields = new ArrayList<>();

	public static ParsedTextTO withFullText(String txtSrcId, String txtType, String fullText) {
        ParsedTextTO textTO = new ParsedTextTO();
        textTO.setTextSourceId(txtSrcId);
        textTO.setTextType(txtType);
        textTO.setFullText(fullText);
        return textTO;
    }
}
