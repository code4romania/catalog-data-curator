package ro.code4.curator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.code4.curator.entity.ParsedText;

import java.io.Serializable;
import java.util.List;

@Repository
public interface ParsedTextRepository extends CrudRepository<ParsedText, Serializable> {
	List<ParsedText> findByTextTypeAndTextSourceId(String textType, String textSourceId);
}
