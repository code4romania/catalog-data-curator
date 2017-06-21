package ro.code4.curator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Lob
    private String fullText;
    private String textType;
    private String textSourceId;

    public static Text with(String txtSrcId, String type, String fullText) {
        Text text = new Text();
        text.setFullText(fullText);
        text.setTextSourceId(txtSrcId);
        text.setTextType(type);
        return text;
    }
}
