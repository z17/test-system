package test_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.WorkEntity;
import test_system.repository.WorkRepository;

import java.util.List;

@Service
public class WorkService {

    private final WorkRepository workRepository;

    @Autowired
    public WorkService(final WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public List<WorkEntity> getWorks() {
        return (List<WorkEntity>) workRepository.findAll();
    }

    public WorkEntity getWork(final long id) {
        return workRepository.findOne(id);
    }
}
