package ro.code4.curator.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.code4.curator.Application;
import ro.code4.curator.transferObjects.ParsedInputFieldTO;
import ro.code4.curator.transferObjects.ParsedInputTO;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created on 4/30/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@org.springframework.transaction.annotation.Transactional(noRollbackFor = {})
public class ParsedInputServiceTest {

    // TODO add parsed input merging tests

    @Autowired
    ParsedInputService service;

    @Before
    public void setUp() throws Exception {
        service.list().stream()
                .forEach(e -> service.deleteById(e.getEntityId()));
    }

    @Test
    public void shouldReturnEmptyList_ifDbEmpty() throws Exception {
        assertTrue(service.list().isEmpty());
    }

    @Test
    public void shouldAddThenFindNewEntry_getAll_vs_getById() throws Exception {
        // is empty
        assertTrue(service.list().isEmpty());

        // add one
        service.acceptTextParsing(new ParsedInputTO());

        // list all
        ParsedInputTO entry = service.list().get(0);

        // get by id should return the same
        assertEquals(entry, service.getById(entry.getEntityId()));
    }

    @Test
    public void shouldAddThenFindNewEntry_validateAllFields() throws Exception {
        // is empty
        assertTrue(service.list().isEmpty());
        // add one
        String fullText = "Sus numitului Aurel Inculpat, i se aduc urmatoarele acuzatii...";
        String parsedText = "Aurel Inculpat";
        ParsedInputFieldTO fieldTO = new ParsedInputFieldTO();
        fieldTO.setFieldName("inculpat");
        fieldTO.setParsedValue(parsedText);
        fieldTO.setParserId("parser-inculpat");
        fieldTO.setStartPos(fullText.indexOf(parsedText));
        fieldTO.setEndPos(fullText.indexOf(parsedText) + parsedText.length());
        ParsedInputTO entry = new ParsedInputTO();
        entry.setTextType("Dosar");
        entry.setTextSourceId("DNA");
        entry.setReviewed(true);
        entry.setFullText(fullText);
        entry.setParsedFields(asList(fieldTO));

        ParsedInputTO savedEntry = service.acceptTextParsing(entry);

        // compare original to returned obj on save operation
        assertEquals(fullText, savedEntry.getFullText());
        assertEquals("DNA", savedEntry.getTextSourceId());
        assertEquals("Dosar", savedEntry.getTextType());
        assertTrue(savedEntry.isReviewed());
        assertEquals(1, savedEntry.getParsedFields().size());
        ParsedInputFieldTO to = savedEntry.getParsedFields().get(0);
        assertEquals(fieldTO.getEndPos(), to.getEndPos());
        assertEquals(fieldTO.getStartPos(), to.getStartPos());
        assertEquals(fieldTO.getFieldName(), to.getFieldName());
        assertEquals(fieldTO.getParsedValue(), to.getParsedValue());
        assertEquals(fieldTO.getParserId(), to.getParserId());
        assertEquals(fieldTO.getParsedInputId(), to.getParsedInputId());

        // list all
        ParsedInputTO se = service.list().get(0);

        // compare retrieve result with save obj
        assertEquals(savedEntry.getFullText(), se.getFullText());
        assertEquals(savedEntry.getTextSourceId(), se.getTextSourceId());
        assertEquals(savedEntry.getTextType(), se.getTextType());
        assertTrue(se.isReviewed());
        assertEquals(savedEntry.getParsedFields().size(), se.getParsedFields().size());
        ParsedInputFieldTO seto = savedEntry.getParsedFields().get(0);
        assertEquals(to.getEndPos(), seto.getEndPos());
        assertEquals(to.getStartPos(), seto.getStartPos());
        assertEquals(to.getFieldName(), seto.getFieldName());
        assertEquals(to.getParsedValue(), seto.getParsedValue());
        assertEquals(to.getParserId(), seto.getParserId());
        assertEquals(to.getParsedInputId(), seto.getParsedInputId());
    }

}