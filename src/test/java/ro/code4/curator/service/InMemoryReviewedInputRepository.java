package ro.code4.curator.service;

import ro.code4.curator.entity.ReviewedText;
import ro.code4.curator.repository.ReviewedInputRepository;

import java.io.Serializable;

/**
 * Created:
 * Date: 6/14/17
 * Time: 11:57 PM
 */
class InMemoryReviewedInputRepository implements ReviewedInputRepository {
    
    @Override
    public ReviewedText findByTextTypeAndTextSourceId(String textType, String textSourceId) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public <S extends ReviewedText> S save(S entity) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public <S extends ReviewedText> Iterable<S> save(Iterable<S> entities) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public ReviewedText findOne(Serializable serializable) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public boolean exists(Serializable serializable) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public Iterable<ReviewedText> findAll() {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public Iterable<ReviewedText> findAll(Iterable<Serializable> serializables) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public long count() {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void delete(Serializable serializable) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void delete(ReviewedText entity) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void delete(Iterable<? extends ReviewedText> entities) {
        throw new RuntimeException("Not implemented !");
    }

    @Override
    public void deleteAll() {
        throw new RuntimeException("Not implemented !");
    }
}
