package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.TaskEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
}
