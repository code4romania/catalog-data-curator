package ro.code4.curator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ro.code4.curator.converter.FileUtils;
import ro.code4.curator.converter.ParsedTextConverter;
import ro.code4.curator.converter.ReviewedTextConverter;
import ro.code4.curator.entity.*;
import ro.code4.curator.repository.UserRepository;
import ro.code4.curator.transferObjects.ParsedTextTO;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * On app startup, insert some dummy data to be able to easily test the UI
 */
@Component
public class MockData {

    private static final Logger log = LoggerFactory.getLogger(MockData.class);

    @Autowired
    ParsedTextManager parsedInputService;

    @Autowired
    ReviewedTextManager IReviewedInputService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewedTextConverter reviewedInputConverter;

    @Autowired
    ParsedTextConverter parsedInputConverter;

    @Value("${test.data.enabled:false}")
    private boolean isEnabled = false;

    @Value("${test.data.folder:./}")
    private String testDataFolder;

    public User testUser = new User("user", "password");
    public List<ParsedText> mockParsedInputs = new ArrayList<>();
    public List<ReviewedText> mockReviewedInputs = new ArrayList<>();

    @PostConstruct
    public void initDummyData() {
        if (!isEnabled) {
            return;
        }

        log.info("inserting test data...");
        userRepository.save(getTestUser());

        addParsedInput();

        addReviewedInput();
    }

    private void addReviewedInput() {
        if (mockParsedInputs.isEmpty())
            return;

        // get initial parsing
        ParsedText parsedInput = mockParsedInputs.get(0);

        // make review
        ReviewedText reviewedInput = toEntity(toTo(parsedInput));
        add(reviewedInput, parsedInput);

        // set initial parsing to reviewed
        parsedInput.setReviewed(true);
        parsedInputService.submitParsedText(toTo(parsedInput));
    }

    private void addParsedInput() {
        ParsedText entity = null;
        String path = getTestDataFolder();
        Collection<File> files = org.apache.commons.io.FileUtils.listFiles(new File(path),
                new String[]{"json"}, false);
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            String filePath = iterator.next().getAbsolutePath();
            entity = ParsedText.from(FileUtils.readFile(filePath));
            add(entity);
        }
    }

    private String getTestDataFolder() {
        if (testDataFolder == null)
            return "";

        testDataFolder = testDataFolder.trim();

        return MockData.class.getResource(testDataFolder).getFile();
    }

    private void add(ReviewedText entity, ParsedText input) {
        ParsedTextTO to = IReviewedInputService.submitReview(
                input.getId(),
                toTo(entity));
        mockReviewedInputs.add(toEntity(to));
    }

    private ParsedTextTO toTo(ReviewedText entity1) {
        return reviewedInputConverter.toTO(entity1);
    }

    private void add(ParsedText entity) {
        ParsedTextTO to = parsedInputService.submitParsedText(
                toTo(entity));
        mockParsedInputs.add(parsedInputConverter.toEntity(to));
    }

    private ReviewedText toEntity(ParsedTextTO to) {
        return reviewedInputConverter.toEntity(to);
    }

    private ParsedTextTO toTo(ParsedText parsedInput) {
        return parsedInputConverter.toTo(parsedInput);
    }

    public User getTestUser() {
        return testUser;
    }
}
