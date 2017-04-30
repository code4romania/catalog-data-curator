package ro.code4.curator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ro.code4.curator.converter.JsonUtils;
import ro.code4.curator.transferObjects.ParsedInputTO;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ParsedInput {
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
	private List<ParsedInputField> parsedFields = new ArrayList<>();

	public static ParsedInput from(String json) {
		try {
			return JsonUtils.parseJsonObj(json, ParsedInput.class);
		} catch (IOException e) {
			throw new RuntimeException("failed to parse json " + json);
		}
	}

    public boolean hasEqualFullText(ParsedInputTO parsedInputTO) {
        return getFullText().trim().equals(parsedInputTO.getFullText().trim());
    }
}
