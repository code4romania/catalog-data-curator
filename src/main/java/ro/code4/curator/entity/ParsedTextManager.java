package ro.code4.curator.entity;

import ro.code4.curator.transferObjects.ParsedTextTO;

import java.util.List;

/**
 *  Handles CRUD operations on newly submitted texts.
 *  This service does not handle reviewed texts.
 */
public interface ParsedTextManager {

    /**
     * persists a newly parsed text
     * It will search for duplicates based on source id and text type. If found will merge findings list.
     *
     * @param newEntry parsed document
     * @return
     */
    ParsedTextTO submitParsedText(ParsedTextTO newEntry);

    /**
     * Returns all parsed texts (only parsed, not reviewed or any other type)
     *
     * @return
     */
    List<ParsedTextTO> getAllParsedTexts();

    /**
     *
     * @param id
     * @return a specific parsed text
     */
    ParsedTextTO getParsedTextById(int id);

    /**
     * removed a parsed text by id
     *
     * @param id
     */
    void deleteParsedTextById(int id);
}
