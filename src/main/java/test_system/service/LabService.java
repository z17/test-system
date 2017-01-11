package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test_system.entity.Lab;
import test_system.entity.WorkPhase;

@Component
public class LabService {
    private final WorkService workService;
    private final WorkExecutionService workExecutionService;

    @Autowired
    public LabService(WorkService workService, WorkExecutionService workExecutionService) {
        this.workService = workService;
        this.workExecutionService = workExecutionService;
    }

    public String labPage(final long workId) {
        val work = workService.getWork(workId);

        if (work.getLab() == Lab.EMPTY) {
            throw new RuntimeException();
        }

        val processingWork = workExecutionService.getProcessingWork(workId);

        if (processingWork == null) {
            throw new RuntimeException("Access denied");
        }

        if (processingWork.getPhase() != WorkPhase.LAB) {
            throw new RuntimeException("Access denied");
        }

        return work.getLab().getTemplate();
    }

    public void finishLab(final long workId, final Object data) {

    }
}
