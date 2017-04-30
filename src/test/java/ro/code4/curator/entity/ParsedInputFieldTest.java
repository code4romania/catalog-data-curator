package ro.code4.curator.entity;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created on 4/30/17.
 */
public class ParsedInputFieldTest {

    @Test
    public void verifyNoCircularReferenceProblems() throws Exception {
        ParsedInput input = new ParsedInput();

        ParsedInputField parentField = new ParsedInputField();
        input.getParsedFields().add(parentField);
        parentField.setParsedInputId(input);

        ParsedInputField field = new ParsedInputField();
        field.setParsedInputId(input);
        field.setParentField(parentField);

        input.setParsedFields(asList(field));

        assertTrue(input.toString().length() > 0);
    }

    @Test
    public void verifyNoCircularReferenceProblems_fieldParent() throws Exception {
        ParsedInputField parent = new ParsedInputField();
        ParsedInputField child = new ParsedInputField();

        child.setParentField(parent);
        parent.setParentField(child);

        assertTrue(parent.toString().length() > 0);
        assertTrue(child.toString().length() > 0);
    }
}