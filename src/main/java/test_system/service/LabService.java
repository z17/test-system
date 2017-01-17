package test_system.service;

import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import test_system.entity.*;
import test_system.lab.HolographyLab;
import test_system.lab.LabResult;
import test_system.lab.LabStrategy;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class LabService {
    private final WorkService workService;
    private final WorkExecutionService workExecutionService;

    // todo: to properties
    public static final Path LAB_FILES_FOLDER = Paths.get("C:\\Java\\projects\\test-system\\labs");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(LAB_FILES_FOLDER);
    }

    @Autowired
    public LabService(WorkService workService, WorkExecutionService workExecutionService) {
        this.workService = workService;
        this.workExecutionService = workExecutionService;
    }

    public String labPage(final long workId) {
        val work = checkLab(workId);

        return work.getLab().getTemplate();
    }

    public String processLab(final long workId, final MultipartFile file, final Map<String, String> data) {
        val work = checkLab(workId);

        val processingWork = workExecutionService.getProcessingWork(workId);

        try {
            val filePath = getLabFileName(processingWork, file);
            file.transferTo(filePath.toFile());
            data.put(HolographyLab.FILE_KEY, filePath.toString());
            LabResult result = runLab(work.getLab(), processingWork.getId(), data);
            if ( processingWork.getLabResult() == null) {
                processingWork.setLabResult(new LabResultEntity());
            }
            processingWork.getLabResult().setText(result.toJson());
            workExecutionService.update(processingWork);
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

    private LabResult runLab(final Lab lab, final long executionId, final Map<String, String> data) {
        try {
            LabStrategy labStrategy = lab.getStrategy().newInstance();
            return labStrategy.process(data, LAB_FILES_FOLDER, executionId + "-");

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Path getLabFileName(final WorkExecutionEntity workExecutionEntity, final MultipartFile file) {
        val originalFilename = file.getOriginalFilename();
        val name = FilenameUtils.removeExtension(originalFilename);
        val extension = FilenameUtils.getExtension(originalFilename);
        return LAB_FILES_FOLDER.resolve(workExecutionEntity.getId() + "-" + name + "." + extension);
    }

}
