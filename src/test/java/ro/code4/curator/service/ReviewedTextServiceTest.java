package ro.code4.curator.service;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import ro.code4.curator.entity.ParsedText;
import ro.code4.curator.entity.ReviewedText;
import ro.code4.curator.repository.ParsedTextRepository;
import ro.code4.curator.repository.ReviewedTextRepository;
import ro.code4.curator.transferObjects.ParsedTextTO;

/**
 * Created:
 * Date: 6/14/17
 * Time: 8:24 PM
 */
public class ReviewedTextServiceTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private ReviewTextService service;
    private ReviewedTextRepository reviewedInputRepo;
    private ParsedTextRepository parsedInputRepository;


    @Before
    public void setUp() throws Exception {
        service = new ReviewTextService();
        reviewedInputRepo = context.mock(ReviewedTextRepository.class);
        service.setReviewedInputRepo(reviewedInputRepo);
        parsedInputRepository = context.mock(ParsedTextRepository.class);
        service.setParsedInputRepository(parsedInputRepository);
    }

    @Test
    @Ignore // TODO clarify usecase
    public void submitReviewedInput() throws Exception {
        ParsedTextTO reviewedFinding = new ParsedTextTO();
        reviewedFinding.setTextSourceId("src-id");
        reviewedFinding.setTextType("type");

        ReviewedText value = new ReviewedText();
        value.setTextType(reviewedFinding.getTextType());
        value.setTextSourceId(reviewedFinding.getTextSourceId());

            ParsedText x = new ParsedText();
        context.checking(new Expectations() {{
            oneOf(reviewedInputRepo).findByTextTypeAndTextSourceId("type", "src-id");
            will(returnValue(null));
            oneOf(reviewedInputRepo).findByTextTypeAndTextSourceId("type", "src-id");
            will(returnValue(new ReviewedText()));
            oneOf (reviewedInputRepo).save(with(value));
            will(returnValue(value));
            oneOf (parsedInputRepository).findOne(with(1));
            will(returnValue(x));
            oneOf (parsedInputRepository).save(with(x));
            will(returnValue(x));
        }});

        ParsedTextTO updatedFinding = service.submitReview(1, reviewedFinding);

        
    }

    @Test
    public void list() throws Exception {
    }

}
