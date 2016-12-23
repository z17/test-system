package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.WorkAnswerEntity;

public interface WorkAnswerRepository extends CrudRepository<WorkAnswerEntity, Long> {
}
