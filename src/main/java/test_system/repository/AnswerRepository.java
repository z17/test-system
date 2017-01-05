package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.AnswerEntity;

public interface AnswerRepository extends CrudRepository<AnswerEntity, Long>{
}
