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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // TODO are we sure ? always working with full text ?
    private Text text;
    private String textType;
    private String textSourceId;

    @OneToMany(mappedBy = "reviewedInputId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReviewedFinding> reviewedFields = new ArrayList<>();

    public void setText(Text text) {
        if (text == null) return;

        this.text = text;
        setTextType(text.getTextType());
        setTextSourceId(text.getTextSourceId());
    }
}
