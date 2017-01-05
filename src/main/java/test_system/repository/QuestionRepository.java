package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.QuestionEntity;

public interface QuestionRepository extends CrudRepository<QuestionEntity, Long> {
}
