package test_system.service;

import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import test_system.entity.Lab;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;
import test_system.lab.LabStrategy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class LabService {
    private final WorkService workService;
    private final WorkExecutionService workExecutionService;

    // todo: to properties
    private final Path labFilesFolder = Paths.get("C:\\Java\\projects\\test-system");

    public final static String FILE_DATA_KEY = "lab-file";

    @Autowired
    public LabService(WorkService workService, WorkExecutionService workExecutionService) {
        this.workService = workService;
        this.workExecutionService = workExecutionService;
    }

    public String labPage(final long workId) {
        val work = checkLab(workId);

        return work.getLab().getTemplate();
    }

    public String processLab(final long workId, final MultipartFile file, final Map<String, String> data){
        val work = checkLab(workId);

        val processingWork = workExecutionService.getProcessingWork(workId);

        try {
            val filePath = getLabFileName(processingWork, file);
            file.transferTo(filePath.toFile());
            data.put(FILE_DATA_KEY, filePath.toString());
            runLab(work.getLab(), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return work.getLab().getTemplate();
    }

    public void finishLab(final long workId, final Map<String, String> data) {
        val work = checkLab(workId);

//        runLab(work.getLab(), data);
    }

    private WorkEntity checkLab(final long workId) {
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
        return work;
    }

    private void runLab(final Lab lab, final Map<String, String> data) {
        try {
            LabStrategy labStrategy = lab.getStrategy().newInstance();
            labStrategy.process(data);

            // todo: save data
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Path getLabFileName(final WorkExecutionEntity workExecutionEntity, final MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String name = FilenameUtils.removeExtension(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);

        return labFilesFolder.resolve(name + "-" + workExecutionEntity.getId() + "." + extension);
    }
}
