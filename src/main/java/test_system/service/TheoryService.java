package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.TheoryEntity;
import test_system.exception.NotFoundException;
import test_system.repository.TheoryRepository;

@Service
public class TheoryService {

    private final TheoryRepository theoryRepository;
    private final WorkExecutionService workExecutionService;

    @Autowired
    public TheoryService(TheoryRepository theoryRepository, WorkService workService, WorkExecutionService workExecutionService) {
        this.theoryRepository = theoryRepository;
        this.workExecutionService = workExecutionService;
    }

    TheoryEntity getTheoryByWorkId(final long workId) {
        val theory = theoryRepository.findByWorkId(workId);

        if (theory == null) {
            throw new NotFoundException("Theory not found");
        }
        return theory;
    }

    public TheoryEntity theoryPage(final long workId) {
        val theory = getTheoryByWorkId(workId);

        workExecutionService.startWork(workId);
        return theory;
    }

    void create(final long createdWorkId, final String theory) {
        final TheoryEntity theoryEntity = new TheoryEntity();
        theoryEntity.setText(theory);
        theoryEntity.setWorkId(createdWorkId);
        theoryRepository.save(theoryEntity);
    }

    void update(final long workId, final String theoryText) {
        val theory = getTheoryByWorkId(workId);
        theory.setText(theoryText);
        theoryRepository.save(theory);
    }

    void deleteByWorkId(final long workId) {
        theoryRepository.deleteByWorkId(workId);
    }
}
