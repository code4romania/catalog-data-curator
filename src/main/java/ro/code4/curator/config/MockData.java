package ro.code4.curator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ro.code4.curator.entity.ParsedInput;
import ro.code4.curator.entity.ReviewedInput;
import ro.code4.curator.entity.User;
import ro.code4.curator.repository.ParsedInputRepository;
import ro.code4.curator.repository.ReviewedInputRepository;
import ro.code4.curator.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * On app startup, insert some dummy data to be able to easily test the UI
 */
@Configuration
public class MockData {

    private static final Logger log = LoggerFactory.getLogger(MockData.class);

    @Autowired
    ParsedInputRepository parsedInputRepository;

    @Autowired
    ReviewedInputRepository reviewedInputRepository;

    @Autowired
    UserRepository userRepository;

    public User testUser = new User("user", "password");
    public ArrayList<ParsedInput> mockParsedInputs = new ArrayList<>();
    public ArrayList<ReviewedInput> mockReviewedInputs = new ArrayList<>();

    @PostConstruct
    public void initDummyData() {
        log.info("inserting test data...");

        userRepository.save(getTestUser());

        ParsedInput entity = new ParsedInput();
        entity.setFullText("this is full text parsed");
        parsedInputRepository.save(entity);
        mockParsedInputs.add(entity);

        ReviewedInput entity1 = new ReviewedInput();
        entity1.setFullText("this is full text reviewed");
        reviewedInputRepository.save(entity1);
        mockReviewedInputs.add(entity1);
    }

    public User getTestUser() {
        return testUser;
    }
}