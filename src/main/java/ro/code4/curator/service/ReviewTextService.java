package ro.code4.curator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.code4.curator.converter.ReviewedTextConverter;
import ro.code4.curator.entity.ParsedText;
import ro.code4.curator.entity.ReviewedText;
import ro.code4.curator.entity.ReviewedTextManager;
import ro.code4.curator.repository.ParsedTextRepository;
import ro.code4.curator.repository.ReviewedInputRepository;
import ro.code4.curator.transferObjects.ParsedTextTO;
import ro.code4.curator.transferObjects.ShallowReviewedTextTO;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewTextService implements ReviewedTextManager {

    @Autowired
    private ReviewedInputRepository reviewedInputRepo;

    @Autowired
    private ParsedTextRepository parsedInputRepository;

    @Autowired
    private ReviewedTextConverter converter = new ReviewedTextConverter();


    @Override
    public ParsedTextTO submitReview(int parsedInputId, ParsedTextTO reviewedInputTO) {
        // check if there is already a reviewed input entry for this textType + id
        String textType = reviewedInputTO.getTextType();
        String sourceId = reviewedInputTO.getTextSourceId();
        ReviewedText existingEntity = reviewedInputRepo.findByTextTypeAndTextSourceId(textType, sourceId);

        if (existingEntity != null) {
//            existingEntity.getReviewedFields()
//                    .stream()
//                    .forEach(e -> e.());
//                    ;

            // if yes, throw exception for now
            // TODO need to implement logic
            throw new EntityExistsException(
                    "A reviewed record for this object already exists. " +
                            "Sending updates to reviewed instances is not supported at this time.");
        } else {
            // if not, convert to an entity
            ReviewedText entity = converter.toEntity(reviewedInputTO);

            // and save it
            ReviewedText saved = reviewedInputRepo.save(entity);
            entity = reviewedInputRepo.findByTextTypeAndTextSourceId(textType, sourceId);

            // mark the parsed input as reviewed
            ParsedText parsedInput = parsedInputRepository.findOne(parsedInputId);
            if (parsedInput == null)
                throw new EntityNotFoundException(
                        "Entity id not found ! " + parsedInputId);

            parsedInput.setReviewed(true);
            parsedInput.setReviewedInputId(entity.getId());
            parsedInputRepository.save(parsedInput);

            return converter.toTO(saved);
        }
    }

    @Override
    public List<ShallowReviewedTextTO> getAllReviewedTexts() {
        List<ShallowReviewedTextTO> result = new ArrayList<>();
        Iterable<ReviewedText> reviewedInputList = reviewedInputRepo.findAll();

        // for each entity, pick only the shallow copy
        for (ReviewedText entity : reviewedInputList)
            result.add(converter.toShallowTO(entity));

        return result;
    }

    @Override
    public ShallowReviewedTextTO getReviewById(String id) {
        ReviewedText entity = reviewedInputRepo.findOne(Integer.valueOf(id));
        return converter.toShallowTO(entity);
    }

    @Override
    public void deleteReviewById(String id) {
        reviewedInputRepo.delete(Integer.valueOf(id));
    }

    public void setReviewedInputRepo(ReviewedInputRepository reviewedInputRepo) {
        this.reviewedInputRepo = reviewedInputRepo;
    }

    public void setParsedInputRepository(ParsedTextRepository parsedInputRepository) {
        this.parsedInputRepository = parsedInputRepository;
    }

    public void setConverter(ReviewedTextConverter converter) {
        this.converter = converter;
    }
}
