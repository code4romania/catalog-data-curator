package ro.code4.curator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ObjectUtils;
import ro.code4.curator.converter.JsonUtils;
import ro.code4.curator.transferObjects.ParsedTextTO;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity represents a complete text document, including the list of findings, if any.
 * Text is identified by source and type.
 * New findings can be added later as they are found by different parsers.
 *
 * The text can be reviewed by a curator and have it's findings validated.
 * In this process the correct findings get votes increases.
 */
@Entity
@Data
@NoArgsConstructor
public class ParsedText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Lob
    private String fullText;
    private String textType;
    private String textSourceId;
    private boolean reviewed;
    private int reviewedInputId;

    @OneToMany(mappedBy = "parsedInputId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TextFinding> parsedFields = new ArrayList<>();

    public static ParsedText from(String json) {
        try {
            return JsonUtils.parseJsonObj(json, ParsedText.class);
        } catch (IOException e) {
            throw new RuntimeException("failed to parse json " + json, e);
        }
    }

    /**
     * If true, both texts refer to the same document.
     *
     * @param textSourceId
     * @param textType
     * @return
     */
    public boolean isSameText(String textSourceId, String textType) {
        return ObjectUtils.equals(textSourceId, this.textSourceId)
                && ObjectUtils.equals(textType, this.textType);
    }

    public boolean hasEqualFullText(ParsedTextTO parsedInputTO) {
        return getFullText().trim().equals(parsedInputTO.getFullText().trim());
    }

    public static ParsedText withId(int id) {
        ParsedText text = new ParsedText();
        text.setId(id);
        return text;
    }
}
