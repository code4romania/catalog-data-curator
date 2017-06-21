package ro.code4.curator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.code4.curator.entity.Text;

import java.io.Serializable;

/**
 * Created on 4/20/17.
 */
@Repository
public interface TextRepository extends CrudRepository<Text, Serializable> {

    Text findByTextTypeAndTextSourceId(String textType, String textSourceId);

    void deleteByTextTypeAndTextSourceId(String textType, String textSourceId);

}
