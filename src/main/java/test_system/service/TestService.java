package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.TestEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
import test_system.exception.NotFoundException;
import test_system.repository.TestRepository;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final WorkService workService;
    private final UserService userService;

    @Autowired
    public TestService(TestRepository testRepository, WorkService workService, UserService userService) {
        this.testRepository = testRepository;
        this.workService = workService;
        this.userService = userService;
    }

    public TestEntity testPage(final long workId) {
        val workExecution = workService.getProcessingWork(workId);

        if (workExecution == null) {
            throw new NotFoundException("You haven't started this work");
        }

        if (workExecution.getPhase() != WorkPhase.THEORY) {
            throw new RuntimeException("You haven't access to this page");
        }

        val test = testRepository.findByWorkId(workId);

        if (test == null) {
            throw new NotFoundException("Test not found");
        }

        workExecution.setPhase(WorkPhase.TEST);
        workService.updateWorkExecution(workExecution);

        return test;
    }
}
