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
        ReviewedInput input = new ReviewedInput();

        ReviewedInputField parentField = new ReviewedInputField();
        input.getReviewedFields().add(parentField);
        parentField.setReviewedInputId(input);

        ReviewedInputField field = new ReviewedInputField();
        field.setReviewedInputId(input);
        field.setParentField(parentField);

        input.setReviewedFields(asList(field));

        assertTrue(input.toString().length() > 0);
    }

    @Test
    public void verifyNoCircularReferenceProblems_fieldParent() throws Exception {
        ReviewedInputField parent = new ReviewedInputField();
        ReviewedInputField child = new ReviewedInputField();

        child.setParentField(parent);
        parent.setParentField(child);

        assertTrue(parent.toString().length() > 0);
        assertTrue(child.toString().length() > 0);
    }


}