package ro.code4.curator.entity;

import ro.code4.curator.transferObjects.ParsedTextTO;
import ro.code4.curator.transferObjects.ShallowReviewedTextTO;

import java.util.List;

/**
 * Manages user review process for parsed texts.
 */
public interface ReviewedTextManager {

    /**
     * Will persist review.
     * Will lookup other reviews and merge with them
     *
     * @param parsedInputId
     * @param reviewedInputTO
     * @return
     */
    ParsedTextTO submitReview(int parsedInputId, ParsedTextTO reviewedInputTO);

    /**
     * Retrieves all text reviews
     *
     * @return
     */
    List<ShallowReviewedTextTO> getAllReviewedTexts();

    /**
     * Retrieves a text review
     *
     * @param id
     * @return
     */
    ShallowReviewedTextTO getReviewById(String id);

    /**
     * Deletes a text review
     *
     * @param id
     */
    void deleteReviewById(String id);
}
