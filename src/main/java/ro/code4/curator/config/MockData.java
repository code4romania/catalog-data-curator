package ro.code4.curator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ro.code4.curator.converter.FileUtils;
import ro.code4.curator.converter.ParsedInputConverter;
import ro.code4.curator.converter.ReviewedInputConverter;
import ro.code4.curator.entity.ParsedInput;
import ro.code4.curator.entity.ReviewedInput;
import ro.code4.curator.entity.User;
import ro.code4.curator.repository.UserRepository;
import ro.code4.curator.service.ParsedInputService;
import ro.code4.curator.service.ReviewedInputService;
import ro.code4.curator.transferObjects.ParsedInputTO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * On app startup, insert some dummy data to be able to easily test the UI
 */
@Component
public class MockData {

    private static final Logger log = LoggerFactory.getLogger(MockData.class);

    @Autowired
    ParsedInputService parsedInputService;

    @Autowired
    ReviewedInputService reviewedInputService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewedInputConverter reviewedInputConverter;

    @Autowired
    ParsedInputConverter parsedInputConverter;

    @Value("${test.data.enabled:false}")
    private boolean isEnabled = false;

    public User testUser = new User("user", "password");
    public ArrayList<ParsedInput> mockParsedInputs = new ArrayList<>();
    public ArrayList<ReviewedInput> mockReviewedInputs = new ArrayList<>();

    @PostConstruct
    public void initDummyData() {
        if(!isEnabled) {
            return;
        }

        log.info("inserting test data...");
        userRepository.save(getTestUser());

        addParsedInput();

        addReviewedInput();
    }

    private void addReviewedInput() {
        // get initial parsing
        ParsedInput parsedInput = mockParsedInputs.get(0);

        // make review
        ReviewedInput reviewedInput = toEntity(toTo(parsedInput));
        add(reviewedInput, parsedInput);

        // set initial parsing to reviewed
        parsedInput.setReviewed(true);
        parsedInputService.acceptTextParsing(toTo(parsedInput));
    }

    private void addParsedInput() {
        ParsedInput entity = null;
        entity = ParsedInput.from(FileUtils.readFile("parsedInput_DNA_8118.json"));
        add(entity);
        entity = ParsedInput.from(FileUtils.readFile("parsedInput_DNA_8119.json"));
        add(entity);
    }

    private void add(ReviewedInput entity, ParsedInput input) {
        ParsedInputTO to = reviewedInputService.submitReviewedInput(
                input.getId(),
                toTo(entity));
        mockReviewedInputs.add(toEntity(to));
    }

    private ParsedInputTO toTo(ReviewedInput entity1) {
        return reviewedInputConverter.toTO(entity1);
    }

    private void add(ParsedInput entity) {
        ParsedInputTO to = parsedInputService.acceptTextParsing(
                toTo(entity));
        mockParsedInputs.add(parsedInputConverter.toEntity(to));
    }

    private ReviewedInput toEntity(ParsedInputTO to) {
        return reviewedInputConverter.toEntity(to);
    }

    private ParsedInputTO toTo(ParsedInput parsedInput) {
        return parsedInputConverter.toTo(parsedInput);
    }

    public User getTestUser() {
        return testUser;
    }
}