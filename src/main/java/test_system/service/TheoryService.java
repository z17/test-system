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
    private final WorkService workService;

    @Autowired
    public TheoryService(TheoryRepository theoryRepository, WorkService workService) {
        this.theoryRepository = theoryRepository;
        this.workService = workService;
    }

    public TheoryEntity theoryPage(final long workId) {
        workService.startWork(workId);

        val theory = theoryRepository.findByWorkId(workId);
        if (theory == null) {
            throw new NotFoundException("Theory not found");
        }
        return theory;
    }
}
