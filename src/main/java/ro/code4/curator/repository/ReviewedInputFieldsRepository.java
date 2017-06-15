package ro.code4.curator.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.code4.curator.entity.ReviewedTextFinding;

@Repository
public interface ReviewedInputFieldsRepository extends CrudRepository<ReviewedTextFinding, Serializable> {

}
