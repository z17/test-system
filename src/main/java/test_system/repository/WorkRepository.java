package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.WorkEntity;

public interface WorkRepository extends CrudRepository<WorkEntity, Long> {
}
