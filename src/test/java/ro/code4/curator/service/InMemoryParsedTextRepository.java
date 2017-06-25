package ro.code4.curator.service;

import ro.code4.curator.entity.ParsedText;
import ro.code4.curator.repository.ParsedTextRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created:
 * Date: 6/14/17
 * Time: 11:44 PM
 */
class InMemoryParsedTextRepository implements ParsedTextRepository {

    List<ParsedText> texts = new ArrayList<>();
    private int maxId;

    @Override
    public List<ParsedText> findByTextTypeAndTextSourceId(String textType, String textSourceId) {
        List found = texts.stream()
                .filter(text -> text.isSameText(textSourceId, textType))
                .collect(Collectors.toList());

        return found;
    }

    @Override
    public <S extends ParsedText> S save(S entity) {
        ParsedText persistedInstance = null;
        for (ParsedText text : texts) {
            if (text.equals(entity)) {
                persistedInstance = text;
                break;
            }
        }

        if (persistedInstance != null) {
            texts.remove(persistedInstance);
        } else {
            entity.setId(maxId++);
        }

        texts.add(entity);

        return entity;
    }

    @Override
    public <S extends ParsedText> Iterable<S> save(Iterable<S> entities) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public ParsedText findOne(Serializable serializable) {
        Optional<ParsedText> first = texts.stream().filter(text -> text.getId() == (Integer.valueOf(serializable.toString()))).findFirst();

        if (first.isPresent())
            return first.get();

        return null;
    }

    @Override
    public boolean exists(Serializable serializable) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public Iterable<ParsedText> findAll() {
        return texts;
    }

    @Override
    public Iterable<ParsedText> findAll(Iterable<Serializable> serializables) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public long count() {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void delete(Serializable serializable) {
        List filtered = texts.stream().filter(text -> text.getId() != Integer.valueOf(serializable.toString()))
                .collect(Collectors.toList());
        texts = filtered;
    }

    @Override
    public void delete(ParsedText entity) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void delete(Iterable<? extends ParsedText> entities) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void deleteAll() {
        throw new RuntimeException("Not implemented !");
    }
}
