package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.UserEntity;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;

public interface WorkExecutionRepository extends CrudRepository<WorkExecutionEntity, Long> {
        WorkExecutionEntity findByUserAndPhaseNot(final UserEntity user, final WorkPhase phase);

        WorkExecutionEntity findByUserAndWork(final UserEntity user, final WorkEntity work);
}