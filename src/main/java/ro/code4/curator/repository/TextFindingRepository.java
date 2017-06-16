package ro.code4.curator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.code4.curator.entity.TextFinding;

import java.io.Serializable;

@Repository
public interface TextFindingRepository extends CrudRepository<TextFinding, Serializable> {

}
