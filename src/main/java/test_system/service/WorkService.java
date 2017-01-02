package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test_system.data.WorkCreateData;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
import test_system.exception.NotFoundException;
import test_system.repository.WorkExecutionRepository;
import test_system.repository.WorkRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class WorkService {

    private final WorkRepository workRepository;

    private final WorkExecutionRepository workExecutionRepository;

    private final UserService userService;

    @Autowired
    private TheoryService theoryService;

    @Autowired
    private TestService testService;

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

    WorkExecutionEntity workProcess(final long workId, final WorkPhase requestPhase) {
        WorkExecutionEntity workExecution = getProcessingWork(workId);

        WorkPhase currentPhase = workExecution != null ? workExecution.getPhase() : null;
        if (!WorkPhase.isTransitionAccess(currentPhase, requestPhase)) {
            throw new RuntimeException("Access is denied");
        }

        if (workExecution == null) {
            return startWork(workId);
        }

        workExecution.setPhase(requestPhase);
        return workExecutionRepository.save(workExecution);
    }


    WorkExecutionEntity finishTest(final WorkExecutionEntity work, final int correctAmount, final int allAmount) {
        work.setCorrectQuestionsAmount(correctAmount);
        work.setQuestionsAmount(allAmount);
        work.setEndTime(new Timestamp(System.currentTimeMillis()));
        return workExecutionRepository.save(work);
    }

    private WorkExecutionEntity startWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = getWork(workId);

        val currentExecutions = workExecutionRepository.findByUserAndPhaseNot(user, WorkPhase.FINISHED);
        if (!currentExecutions.isEmpty()) {
            if (currentExecutions.size() > 1) {
                throw new RuntimeException("Unknown Error");
            }

            val currentExecution = currentExecutions.get(0);
            if (!currentExecution.getWork().equals(work) || currentExecution.getPhase() != WorkPhase.THEORY) {
                throw new RuntimeException("Some work already started");
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

    private WorkExecutionEntity getProcessingWork(final long workId) {
        val user = userService.getCurrentUser();
        val work = getWork(workId);
        return workExecutionRepository.findByUserAndWorkAndPhaseNot(user, work, WorkPhase.FINISHED);
    }

    public void createWork(final WorkCreateData data) {
        WorkEntity work = new WorkEntity();
        work.setName(data.getTitle());
        work.setDescription(data.getDescription());
        val createdWork = workRepository.save(work);

        theoryService.create(createdWork.getId(), data.getTheory());

        testService.create(createdWork.getId(), data.getTestDescription(), data.getQuestions());
    }
}
