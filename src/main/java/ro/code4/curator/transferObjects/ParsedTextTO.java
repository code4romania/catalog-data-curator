package ro.code4.curator.transferObjects;

import lombok.Data;
import lombok.NoArgsConstructor;
import ro.code4.curator.entity.Text;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParsedTextTO {

    private int entityId;

    private String textType;

    private String textSourceId;

    private boolean reviewed;

    private List<ParsedTextFindingTO> parsedFields = new ArrayList<>();

    public static ParsedTextTO withTextReference(Text text) {
        ParsedTextTO textTO = new ParsedTextTO();
        textTO.setTextType(text.getTextType());
        textTO.setTextSourceId(text.getTextSourceId());
        return textTO;
    }

    public void setForText(Text text) {
        setTextType(text.getTextType());
        setTextSourceId(text.getTextSourceId());
    }
}

