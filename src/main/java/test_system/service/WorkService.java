package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
import test_system.exception.NotFoundException;
import test_system.repository.WorkExecutionRepository;
import test_system.repository.WorkRepository;

import java.util.List;

@Service
public class WorkService {

    private final WorkRepository workRepository;

    private final WorkExecutionRepository workExecutionRepository;

    private final UserService userService;

    @Autowired
    public WorkService(final WorkRepository workRepository, WorkExecutionRepository workExecutionRepository, UserService userService) {
        this.workRepository = workRepository;
        this.workExecutionRepository = workExecutionRepository;
        this.userService = userService;
    }

    public List<WorkEntity> getWorks() {
        return (List<WorkEntity>) workRepository.findAll();
    }

    public WorkEntity getWork(final long id) {
        val work = workRepository.findOne(id);

        if (work == null) {
            throw new NotFoundException("Work not found");
        }

        return work;
    }

    public void startWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = workRepository.findOne(workId);

        if (work == null) {
            throw new NotFoundException("Work not found");
        }



        val currentExecution = workExecutionRepository.findByUserAndPhaseNot(user, WorkPhase.FINISHED);
        if (currentExecution != null) {
            throw new RuntimeException("Some work already started");
        }

        val execution = new WorkExecutionEntity();
        execution.setUser(user);
        execution.setWork(work);
        execution.setPhase(WorkPhase.THEORY);
        workExecutionRepository.save(execution);
    }

    public WorkExecutionEntity getProcessingWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = workRepository.findOne(workId);
        if (work == null) {
            throw new NotFoundException("Work not found");
        }
        return workExecutionRepository.findByUserAndWork(user, work);
    }

    public WorkExecutionEntity updateWorkExecution(final WorkExecutionEntity workExecution) {
        return workExecutionRepository.save(workExecution);
    }
}
