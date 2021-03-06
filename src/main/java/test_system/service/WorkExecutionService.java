package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
import test_system.exception.AccessDeniedException;
import test_system.exception.NotFoundException;
import test_system.exception.WorkAlreadyExistsException;
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

    WorkExecutionEntity getActiveWork() {
        val user = userService.getCurrentUser();
        return workExecutionRepository.findByUserAndPhaseNot(user, WorkPhase.FINISHED);

    }

    void update(WorkExecutionEntity workExecution) {
        workExecutionRepository.save(workExecution);
    }

    WorkExecutionEntity startWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = workService.getWork(workId);

        val currentExecution = getActiveWork();
        if (currentExecution != null) {
            if (!currentExecution.getWork().equals(work) || currentExecution.getPhase() != WorkPhase.THEORY) {
                throw new WorkAlreadyExistsException();
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
            throw new AccessDeniedException("Your work is in progress yet");
        }

        return workExecutionRepository.findFirstByUserAndWorkAndPhaseOrderByIdDesc(user, work, WorkPhase.FINISHED);
    }
}
