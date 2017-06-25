package ro.code4.curator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.builder.EqualsBuilder;
import ro.code4.curator.converter.JsonUtils;

import javax.persistence.*;
import java.io.IOException;

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

    public static Text from(String json) {
        try {
            return JsonUtils.parseJsonObj(json, Text.class);
        } catch (IOException e) {
            throw new RuntimeException("failed to parse json " + json, e);
        }
    }

    public boolean hasSameText(Text newText) {
        return new EqualsBuilder()
                .append(getFullText(), newText.getFullText())
                .append(getTextSourceId(), newText.getTextSourceId())
                .append(getTextType(), newText.getTextType())
                .isEquals();
    }
}
