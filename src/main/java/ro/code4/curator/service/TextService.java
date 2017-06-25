package ro.code4.curator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.code4.curator.entity.Text;
import ro.code4.curator.repository.TextRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created:
 * Date: 6/21/17
 * Time: 10:09 PM
 */
@Service
public class TextService {

    @Autowired
    private TextRepository textRepository;

    public Text persist(Text newText) {
        Text text = textRepository.findByTextTypeAndTextSourceId(newText.getTextType(), newText.getTextSourceId());
        if (text != null) {
            validateSameTextContent(newText, text);
            return text;
        }
        return textRepository.save(newText);
    }

    private void validateSameTextContent(Text newText, Text text) {
        if (!text.hasSameText(newText))
            throw new RuntimeException("" +
                    "A text with same source and id already exists ! " +
                    "Please review and remove/add proper version.");
    }

    public List<Text> findAll() {
        Iterable<Text> all = textRepository.findAll();
        ArrayList<Text> texts = new ArrayList<>();
        all.forEach(text -> texts.add(text));
        return texts;
    }

    public Text getByTextTypeAndTextSourceId(String type, String id) {
        return textRepository.findByTextTypeAndTextSourceId(type, id);
    }

    public void deleteById(String type, String id) {
        textRepository.deleteByTextTypeAndTextSourceId(type, id);
    }
}
