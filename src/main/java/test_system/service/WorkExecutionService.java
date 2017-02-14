package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
import test_system.exception.CustomRuntimeException;
import test_system.exception.NotFoundException;
import test_system.repository.WorkExecutionRepository;

@Service
public class WorkExecutionService {
    private final WorkExecutionRepository workExecutionRepository;
    private final UserService userService;
    private final WorkService workService;

    @Autowired
    public WorkExecutionService(WorkExecutionRepository workExecutionRepository, UserService userService, WorkService workService) {
        this.workExecutionRepository = workExecutionRepository;
        this.userService = userService;
        this.workService = workService;
    }

    public WorkExecutionEntity get(final long id) {
        val attempt = workExecutionRepository.findOne(id);

        if (attempt == null) {
            throw new NotFoundException("Attempt not found");
        }

        return attempt;
    }

    WorkExecutionEntity getProcessingWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = workService.getWork(workId);
        return workExecutionRepository.findByUserAndWorkAndPhaseNot(user, work, WorkPhase.FINISHED);
    }

    void update(WorkExecutionEntity workExecution) {
        if (workExecution.getId() == null) {
            throw new RuntimeException();
        }
        workExecutionRepository.save(workExecution);
    }

    WorkExecutionEntity startWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = workService.getWork(workId);

        val currentExecutions = workExecutionRepository.findByUserAndPhaseNot(user, WorkPhase.FINISHED);
        if (!currentExecutions.isEmpty()) {
            if (currentExecutions.size() > 1) {
                throw new CustomRuntimeException("Unknown Error");
            }

            val currentExecution = currentExecutions.get(0);
            if (!currentExecution.getWork().equals(work) || currentExecution.getPhase() != WorkPhase.THEORY) {
                throw new CustomRuntimeException("Some work already started");
            } else {
                return currentExecution;
            }
        }

        val execution = new WorkExecutionEntity();
        execution.setUser(user);
        execution.setWork(work);
        execution.setPhase(WorkPhase.THEORY);
        return workExecutionRepository.save(execution);
    }

    WorkExecutionEntity getFinishedAttempt(long workId) {
        val user = userService.getCurrentUser();
        val work = workService.getWork(workId);
        val activeAttempt = workExecutionRepository.findByUserAndWorkAndPhaseNot(user, work, WorkPhase.FINISHED);
        if (activeAttempt != null) {
            throw new CustomRuntimeException("Your work in progress yet");
        }

        return workExecutionRepository.findFirstByUserAndWorkAndPhaseOrderByIdDesc(user, work, WorkPhase.FINISHED);
    }
}
