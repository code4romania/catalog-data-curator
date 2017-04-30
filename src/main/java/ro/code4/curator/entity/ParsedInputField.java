package ro.code4.curator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.ObjectUtils;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@Data
public class ParsedInputField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private ParsedInput parsedInputId;

    @OneToOne
    private ParsedInputField parentField;

    private String fieldName;
    private int startPos;
    private int endPos;
    @Lob
    private String parsedValue;
    private double votes;
    private String parserId;

    public ParsedInputField() {
    }

    public ParsedInputField(int id, ParsedInput parsedInputId, ParsedInputField parentField, String fieldName, int startPos, int endPos,
                            String parsedValue, double votes, String parserId) {
        super();
        this.id = id;
        this.parsedInputId = parsedInputId;
        this.parentField = parentField;
        this.fieldName = fieldName;
        this.startPos = startPos;
        this.endPos = endPos;
        this.parsedValue = parsedValue;
        this.votes = votes;
        this.parserId = parserId;
    }

    public boolean isFieldContentAndPositionMatch(ParsedInputFieldTO newField) {
        // match if the value, start pos and end pos are the same
        return newField.getFieldName().equals(getFieldName())
                && newField.getParsedValue().equalsIgnoreCase(getParsedValue())
                && newField.getStartPos() == getStartPos()
                && newField.getEndPos() == getEndPos();
    }

    public void incrVotes() {
        setVotes(getVotes() + 1);
    }

    @Override
    public String toString() {
        // avoid parent circular ref
        return "ParsedInputField{" +
                "id=" + id +
                ", parsedInputId='" + getParentInpuIdIfNotNull() + '\'' +
                ", parentFieldId='" + getParentFieldIdIfNotNull() + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", parsedValue='" + parsedValue + '\'' +
                ", votes=" + votes +
                ", parserId='" + parserId + '\'' +
                '}';
    }

    private Integer getParentFieldIdIfNotNull() {
        if (parentField == null)
            return null;
        return parentField.getId();
    }

    private Integer getParentInpuIdIfNotNull() {
        if (parsedInputId == null)
            return null;
        return parsedInputId.getId();
    }
}
