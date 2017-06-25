package ro.code4.curator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@Data
public class TextFinding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private ParsedText parsedInputId;

    @OneToOne
    private TextFinding parentField;

    private String fieldName;
    private int startPos;
    private int endPos;
    @Lob
    private String parsedValue;
    private double votes;
    private String parserId;

    public TextFinding() {
    }

    public TextFinding(int id, ParsedText parsedInputId, TextFinding parentField, String fieldName, int startPos, int endPos,
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

    public boolean isFieldContentAndPositionMatch(ParsedTextFindingTO newField) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TextFinding that = (TextFinding) o;

        if (id != that.id) return false;
        if (startPos != that.startPos) return false;
        if (endPos != that.endPos) return false;
        if (Double.compare(that.votes, votes) != 0) return false;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (parsedValue != null ? !parsedValue.equals(that.parsedValue) : that.parsedValue != null) return false;
        return parserId != null ? parserId.equals(that.parserId) : that.parserId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + id;
        result = 31 * result + (parsedInputId != null ? parsedInputId.hashCode() : 0);
        result = 31 * result + (parentField != null ? parentField.hashCode() : 0);
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + startPos;
        result = 31 * result + endPos;
        result = 31 * result + (parsedValue != null ? parsedValue.hashCode() : 0);
        temp = Double.doubleToLongBits(votes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (parserId != null ? parserId.hashCode() : 0);
        return result;
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
