package ro.code4.curator.transferObjects;

import lombok.Data;
import lombok.NoArgsConstructor;
import ro.code4.curator.entity.Text;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParsedTextTO {
    private int entityId;

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "text_id")
    private Text text;
    private String textType;
    private String textSourceId;

    private boolean reviewed;

    private List<ParsedTextFindingTO> parsedFields = new ArrayList<>();

    public static ParsedTextTO withFullText(Text text) {
        ParsedTextTO textTO = new ParsedTextTO();
        textTO.setText(text);
        return textTO;
    }

    public void setText(Text text) {
        if(text==null) return;
        
        this.text = text;
        setTextType(text.getTextType());
        setTextSourceId(text.getTextSourceId());
    }

    public Text getText() {
        return text;
    }
}

