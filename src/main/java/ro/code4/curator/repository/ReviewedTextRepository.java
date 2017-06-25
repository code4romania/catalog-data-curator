package ro.code4.curator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.code4.curator.entity.ReviewedText;

import java.io.Serializable;

@Repository
public interface ReviewedTextRepository extends CrudRepository<ReviewedText, Serializable> {
    ReviewedText findByTextTypeAndTextSourceId(String textType, String textSourceId);
}
