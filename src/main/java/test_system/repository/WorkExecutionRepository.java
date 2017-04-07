package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.UserEntity;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;

import java.util.List;

public interface WorkExecutionRepository extends CrudRepository<WorkExecutionEntity, Long> {
    WorkExecutionEntity findByUserAndPhaseNot(final UserEntity user, final WorkPhase phase);

    WorkExecutionEntity findByUserAndWorkAndPhaseNot(final UserEntity user, final WorkEntity work, final WorkPhase phase);

    List<WorkExecutionEntity> findByWorkAndPhaseOrderByTestStartTimeDesc(final WorkEntity work, final WorkPhase finished);

    List<WorkExecutionEntity> findByWorkAndUser(final WorkEntity work, final UserEntity user);

    WorkExecutionEntity findFirstByUserAndWorkAndPhaseOrderByIdDesc(final UserEntity user, final WorkEntity work, final WorkPhase finished);
}