package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.TestEntity;

public interface TestRepository extends CrudRepository<TestEntity, Long> {
    TestEntity findByWorkId(final long workId);

    void deleteByWorkId(final long workId);
}

