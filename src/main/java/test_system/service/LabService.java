package test_system.service;

import com.google.gson.Gson;
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
import java.io.File;
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
    public static final Path STATIC_FOLDER_PATH = Paths.get("src/main/resources/static/");
    public static final Path LAB_FILES_FOLDER_PATH = STATIC_FOLDER_PATH.resolve("lab");
    public static final String LAB_FILES_FOLDER = "lab";

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(LAB_FILES_FOLDER_PATH);
    }

    @Autowired
    public LabService(WorkService workService, WorkExecutionService workExecutionService) {
        this.workService = workService;
        this.workExecutionService = workExecutionService;
    }

    public LabResult getLabResult(final long workId) {
        val processingWork = checkLab(workId);
        if (processingWork.getLabResult() == null) {
            return null;
        }

        val textResult = processingWork.getLabResult().getText();
        return new Gson().fromJson(textResult, processingWork.getWork().getLab().getResultClass());
    }

    public LabResult processLab(final long workId, final MultipartFile file, final Map<String, String> data) {
        val processingWork = checkLab(workId);

        try {
            val filePath = getLabFileName(processingWork, file.getOriginalFilename());
            file.transferTo(new File(filePath.toAbsolutePath().toString()));
            data.put(HolographyLab.FILE_KEY, filePath.toString());
            LabResult result = runLab(processingWork.getWork().getLab(), processingWork.getId(), data);
            updateLabResult(processingWork, result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLabResult(final WorkExecutionEntity workExecution, final LabResult result) {
        if ( workExecution.getLabResult() == null) {
            val labResultEntity = new LabResultEntity();
            labResultEntity.setWorkExecution(workExecution);
            workExecution.setLabResult(labResultEntity);
        }
        workExecution.getLabResult().setText(result.toJson());
        workExecutionService.update(workExecution);
    }

    public void finishLab(final long workId, final Map<String, String> data) {
        val processingWork = checkLab(workId);

//        runLab(work.getLab(), data);
    }

    private WorkExecutionEntity checkLab(final long workId) {
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
        return processingWork;
    }

    private LabResult runLab(final Lab lab, final long executionId, final Map<String, String> data) {
        try {
            LabStrategy labStrategy = lab.getStrategyClass().newInstance();
            return labStrategy.process(data, LAB_FILES_FOLDER_PATH, executionId + "-");

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Path getLabFileName(final WorkExecutionEntity workExecutionEntity, final String originalFileName) {
        val name = FilenameUtils.removeExtension(originalFileName);
        val extension = FilenameUtils.getExtension(originalFileName);
        return LAB_FILES_FOLDER_PATH.resolve(workExecutionEntity.getId() + "-input-" + name + "." + extension);
    }

    public String getTemplate(final long workId) {
        return workService.getWork(workId).getLab().getTemplate();
    }
}
