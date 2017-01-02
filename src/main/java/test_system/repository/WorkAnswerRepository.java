package test_system.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import test_system.entity.WorkAnswerEntity;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;

import java.util.List;

public interface WorkAnswerRepository extends CrudRepository<WorkAnswerEntity, Long> {
    @Query("SELECT w FROM WorkAnswerEntity w WHERE w.workExecution.work = :workId")
    List<WorkAnswerEntity> findByWork(@Param(value="workId") final WorkEntity work);

    List<WorkAnswerEntity> findByWorkExecution(final WorkExecutionEntity workExecution);
}
