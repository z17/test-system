package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.WorkExecutionEntity;
import test_system.exception.NotFoundException;
import test_system.repository.WorkExecutionRepository;

@Service
public class WorkExecutionService {
    private final WorkExecutionRepository workExecutionRepository;

    @Autowired
    public WorkExecutionService(WorkExecutionRepository workExecutionRepository) {
        this.workExecutionRepository = workExecutionRepository;
    }

    public WorkExecutionEntity get(final long id) {
        val attempt = workExecutionRepository.findOne(id);

        if (attempt == null) {
            throw new NotFoundException("Attempt not found");
        }

        return attempt;
    }
}
