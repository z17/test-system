package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.TestEntity;
import test_system.exception.NotFoundException;
import test_system.repository.TestRepository;

@Service
public class TestService {
    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public TestEntity testPage(final long workId) {
        val test = testRepository.findByWorkId(workId);

        if (test == null) {
            throw new NotFoundException("Test not found");
        }

        return test;
    }
}
