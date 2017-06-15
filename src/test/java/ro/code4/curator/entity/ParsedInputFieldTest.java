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
        ParsedText input = new ParsedText();

        TextFinding parentField = new TextFinding();
        input.getParsedFields().add(parentField);
        parentField.setParsedInputId(input);

        TextFinding field = new TextFinding();
        field.setParsedInputId(input);
        field.setParentField(parentField);

        input.setParsedFields(asList(field));

        assertTrue(input.toString().length() > 0);
    }

    @Test
    public void verifyNoCircularReferenceProblems_fieldParent() throws Exception {
        TextFinding parent = new TextFinding();
        TextFinding child = new TextFinding();

        child.setParentField(parent);
        parent.setParentField(child);

        assertTrue(parent.toString().length() > 0);
        assertTrue(child.toString().length() > 0);
    }
}
