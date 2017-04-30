package ro.code4.curator.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ReviewedInputField {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private ReviewedInput reviewedInputId;

	@OneToOne
	private ReviewedInputField parentField;

	private String fieldName;
	private int startPos;
	private int endPos;
	@Lob
	private String parsedValue;
	private double votes;
	private String parserId;

    @Override
    public String toString() {
        // avoid parent circular ref
        return "ReviewedInputField{" +
                "id=" + id +
                ", fieldName='" + fieldName + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", parsedValue='" + parsedValue + '\'' +
                ", votes=" + votes +
                ", parserId='" + parserId + '\'' +
                '}';
    }
}
