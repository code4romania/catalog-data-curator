package ro.code4.curator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ReviewedText {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    @Lob
	private String fullText;
	private String textType;
	private String textSourceId;

	@OneToMany(mappedBy = "reviewedInputId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ReviewedTextFinding> reviewedFields = new ArrayList<>();
}
