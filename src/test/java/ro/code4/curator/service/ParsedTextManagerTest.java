package ro.code4.curator.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.code4.curator.Application;
import ro.code4.curator.entity.ParsedTextManager;
import ro.code4.curator.transferObjects.ParsedTextFindingTO;
import ro.code4.curator.transferObjects.ParsedTextTO;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created on 4/30/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@org.springframework.transaction.annotation.Transactional(noRollbackFor = {})
public class ParsedTextManagerTest {

    @Autowired
    ParsedTextManager service;

    @Before
    public void setUp() throws Exception {
        service.getAllParsedTexts().stream()
                .forEach(e -> service.deleteParsedTextById(e.getEntityId()));
    }

    @Test
    public void should_ReturnEmptyList_ifDbEmpty() throws Exception {
        assertTrue(service.getAllParsedTexts().isEmpty());
    }

    @Test
    public void should_AddThenFindNewEntry_getAll_vs_getById() throws Exception {
        // is empty
        assertTrue(service.getAllParsedTexts().isEmpty());

        // add one
        service.submitParsedText(new ParsedTextTO());

        // list all
        ParsedTextTO entry = service.getAllParsedTexts().get(0);

        // get by id should return the same
        assertEquals(entry, service.getParsedTextById(entry.getEntityId()));
    }

    @Test
    public void should_AddThenFindNewEntry_validateAllFields() throws Exception {
        // is empty
        assertTrue(service.getAllParsedTexts().isEmpty());
        // add one
        String fullText = "Sus numitului Aurel Inculpat, i se aduc urmatoarele acuzatii...";
        String parsedText = "Aurel Inculpat";
        ParsedTextFindingTO fieldTO = new ParsedTextFindingTO();
        fieldTO.setFieldName("inculpat");
        fieldTO.setParsedValue(parsedText);
        fieldTO.setParserId("parser-inculpat");
        fieldTO.setStartPos(fullText.indexOf(parsedText));
        fieldTO.setEndPos(fullText.indexOf(parsedText) + parsedText.length());
        ParsedTextTO entry = new ParsedTextTO();
        entry.setTextType("Dosar");
        entry.setTextSourceId("DNA");
        entry.setReviewed(true);
        entry.setFullText(fullText);
        entry.setParsedFields(asList(fieldTO));

        ParsedTextTO savedEntry = service.submitParsedText(entry);

        // compare original to returned obj on save operation
        assertEquals(fullText, savedEntry.getFullText());
        assertEquals("DNA", savedEntry.getTextSourceId());
        assertEquals("Dosar", savedEntry.getTextType());
        assertTrue(savedEntry.isReviewed());
        assertEquals(1, savedEntry.getParsedFields().size());
        ParsedTextFindingTO to = savedEntry.getParsedFields().get(0);
        assertEquals(fieldTO.getEndPos(), to.getEndPos());
        assertEquals(fieldTO.getStartPos(), to.getStartPos());
        assertEquals(fieldTO.getFieldName(), to.getFieldName());
        assertEquals(fieldTO.getParsedValue(), to.getParsedValue());
        assertEquals(fieldTO.getParserId(), to.getParserId());
        assertEquals(fieldTO.getParsedInputId(), to.getParsedInputId());

        // list all
        ParsedTextTO se = service.getAllParsedTexts().get(0);

        // compare retrieve result with save obj
        assertEquals(savedEntry.getFullText(), se.getFullText());
        assertEquals(savedEntry.getTextSourceId(), se.getTextSourceId());
        assertEquals(savedEntry.getTextType(), se.getTextType());
        assertTrue(se.isReviewed());
        assertEquals(savedEntry.getParsedFields().size(), se.getParsedFields().size());
        ParsedTextFindingTO seto = savedEntry.getParsedFields().get(0);
        assertEquals(to.getEndPos(), seto.getEndPos());
        assertEquals(to.getStartPos(), seto.getStartPos());
        assertEquals(to.getFieldName(), seto.getFieldName());
        assertEquals(to.getParsedValue(), seto.getParsedValue());
        assertEquals(to.getParserId(), seto.getParserId());
        assertEquals(to.getParsedInputId(), seto.getParsedInputId());
    }

}
