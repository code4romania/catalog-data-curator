package ro.code4.curator.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.code4.curator.entity.ParsedInput;

@Repository
public interface ParsedInputRepository extends CrudRepository<ParsedInput, Serializable> {
	List<ParsedInput> findByTextTypeAndTextSourceId(String textType, String textSourceId);
}
