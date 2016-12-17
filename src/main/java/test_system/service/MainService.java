package test_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.entity.TaskEntity;
import test_system.repository.TaskRepository;

import java.util.List;

@Service
public class MainService {

    private final TaskRepository taskRepository;

    @Autowired
    public MainService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskEntity> getTasks() {
        return (List<TaskEntity>) taskRepository.findAll();
    }
}
