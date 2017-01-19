package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test_system.data.ResultData;
import test_system.data.WorkCreateData;
import test_system.data.WorkData;
import test_system.entity.WorkEntity;
import test_system.exception.NotFoundException;
import test_system.repository.WorkRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class WorkService {

    private final WorkRepository workRepository;

    @Autowired
    private WorkExecutionService workExecutionService;

    @Autowired
    private TheoryService theoryService;

    @Autowired
    private TestService testService;

    @Autowired
    private LabService labService;

    @Autowired
    public WorkService(final WorkRepository workRepository) {
        this.workRepository = workRepository;
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

    public WorkEntity updateWork(final WorkCreateData data) {
        if (data.getId() != null) {
            return editWork(data);
        }
        return createWork(data);
    }

    public void delete(final long id) {
        theoryService.deleteByWorkId(id);
        testService.deleteByWorkId(id);
        workRepository.delete(id);
    }

    public WorkData getWorkData(final long workId) {
        return new WorkData(
                getWork(workId),
                theoryService.getTheoryByWorkId(workId),
                testService.getTestByWorkId(workId)
        );
    }

    private WorkEntity createWork(final WorkCreateData data) {
        WorkEntity work = new WorkEntity();
        work.setName(data.getName());
        work.setDescription(data.getDescription());
        work.setLab(data.getLab());
        val createdWork = workRepository.save(work);

        theoryService.create(createdWork.getId(), data.getTheory());

        testService.create(createdWork.getId(), data.getTestDescription(), data.getQuestions());
        return createdWork;
    }

    private WorkEntity editWork(final WorkCreateData data) {
        val work = getWork(data.getId());

        work.setName(data.getName());
        work.setDescription(data.getDescription());
        work.setLab(data.getLab());

        theoryService.update(work.getId(), data.getTheory());
        testService.update(work.getId(), data.getTestDescription(), data.getQuestions());

        workRepository.save(work);
        return work;
    }

    public ResultData finishPage(final long workId) {
        val workExecutionEntity = workExecutionService.getFinishedAttempt(workId);

        long testDuration = TimeUnit.MILLISECONDS.toMinutes(workExecutionEntity.getTestEndTime().getTime() - workExecutionEntity.getTestStartTime().getTime());
        if (workExecutionEntity.getLabResult() != null) {
            return new ResultData(
                    workExecutionEntity.getQuestionsAmount(),
                    workExecutionEntity.getCorrectQuestionsAmount(),
                    testDuration,
                    labService.getLabData(workExecutionEntity.getLabResult(), workExecutionEntity.getWork().getLab()),
                    workExecutionEntity.getWork().getLab().getResultTemplate()
            );
        }

        return new ResultData(
                workExecutionEntity.getQuestionsAmount(),
                workExecutionEntity.getCorrectQuestionsAmount(),
                testDuration,
                null,
                null
        );

    }
}
