package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.TheoryEntity;
import test_system.entity.WorkEntity;

public interface TheoryRepository extends CrudRepository<TheoryEntity, Long> {
    TheoryEntity findByWorkId(final long workId);

    void deleteByWorkId(final Long workId);
}
