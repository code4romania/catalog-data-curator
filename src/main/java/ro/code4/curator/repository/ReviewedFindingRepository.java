package ro.code4.curator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.code4.curator.entity.ReviewedFinding;

import java.io.Serializable;

@Repository
public interface ReviewedFindingRepository extends CrudRepository<ReviewedFinding, Serializable> {

}
