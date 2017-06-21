package ro.code4.curator.entity;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created on 4/30/17.
 */
public class ReviewedInputTest {

    @Test
    public void verifyNoCircularReferenceProblems() throws Exception {
        ReviewedText input = new ReviewedText();

        ReviewedFinding parentField = new ReviewedFinding();
        input.getReviewedFields().add(parentField);
        parentField.setReviewedInputId(input);

        ReviewedFinding field = new ReviewedFinding();
        field.setReviewedInputId(input);
        field.setParentField(parentField);

        input.setReviewedFields(asList(field));

        assertTrue(input.toString().length() > 0);
    }

    @Test
    public void verifyNoCircularReferenceProblems_fieldParent() throws Exception {
        ReviewedFinding parent = new ReviewedFinding();
        ReviewedFinding child = new ReviewedFinding();

        child.setParentField(parent);
        parent.setParentField(child);

        assertTrue(parent.toString().length() > 0);
        assertTrue(child.toString().length() > 0);
    }


}
