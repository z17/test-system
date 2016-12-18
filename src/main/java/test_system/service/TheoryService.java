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

    @Autowired
    public TheoryService(TheoryRepository theoryRepository) {
        this.theoryRepository = theoryRepository;
    }

    public TheoryEntity theoryPage(final long workId) {
        val theory = theoryRepository.findByWorkId(workId);
        if (theory == null) {
            throw new NotFoundException("Theory not found");
        }
        return theory;
    }
}
