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
import ro.code4.curator.repository.TextRepository;
import ro.code4.curator.repository.UserRepository;
import ro.code4.curator.transferObjects.ParsedTextTO;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.filefilter.FileFilterUtils.asFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.falseFileFilter;

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
    TextRepository textRepository;

    @Autowired
    ReviewedTextConverter reviewedInputConverter;

    @Autowired
    ParsedTextConverter parsedInputConverter;

    @Value("${test.data.enabled:false}")
    private boolean isEnabled = false;

    @Value("${test.data.folder:./}")
    private String testDataFolder;

    public User testUser = new User("user", "password");
    public List<Text> mockTexts = new ArrayList<>();
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
        String path = getTestDataFolder();

        Iterator<File> iterator = getTestParseResults(path, "text");
        while (iterator.hasNext()) {
            String filePath = iterator.next().getAbsolutePath();
            Text text = Text.from(FileUtils.readFile(filePath));
            add(text);
        }

        ParsedText entity = null;
        iterator = getTestParseResults(path, "parse");
        while (iterator.hasNext()) {
            String filePath = iterator.next().getAbsolutePath();
            entity = ParsedText.from(FileUtils.readFile(filePath));
            add(entity);
        }
    }

    private Iterator<File> getTestParseResults(String path, String filenameFilter) {
        Collection<File> files = listFiles(
                new File(path),
                asFileFilter((dir, name) -> name.contains(filenameFilter)), falseFileFilter());
        return files.iterator();
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

    private void add(Text entity) {
        Text text = textRepository.save(entity);
        mockTexts.add(text);
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
