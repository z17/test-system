package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
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

    void updatePhase(final WorkExecutionEntity workExecution, final WorkPhase phase) {
        workExecution.setPhase(phase);
        workExecutionRepository.save(workExecution);
    }
}
