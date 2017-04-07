package test_system.service;

import com.google.gson.Gson;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import test_system.entity.*;
import test_system.exception.AccessDeniedException;
import test_system.exception.WorkAlreadyExistsException;
import test_system.lab.HolographyLab;
import test_system.lab.LabData;
import test_system.lab.LabStrategy;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
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

    public LabData getLabResult(final long workId) {
        val processingWork = checkLab(workId);
        if (processingWork.getLabResult() == null) {
            return null;
        }

        return getLabData(processingWork.getLabResult(), processingWork.getWork().getLab());
    }

    public LabData processLab(final long workId, final MultipartFile file, final Map<String, String> data) {
        val processingWork = checkLab(workId);

        try {
            val filePath = getLabFileName(processingWork, file.getOriginalFilename());
            file.transferTo(new File(filePath.toAbsolutePath().toString()));
            data.put(HolographyLab.FILE_KEY, filePath.toString());
            LabData result = runLab(processingWork.getWork().getLab(), processingWork.getId(), data);
            updateLabResult(processingWork, result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLabResult(final WorkExecutionEntity workExecution, final LabData result) {
        if ( workExecution.getLabResult() == null) {
            val labResultEntity = new LabResultEntity();
            labResultEntity.setWorkExecution(workExecution);
            workExecution.setLabResult(labResultEntity);
        }
        workExecution.getLabResult().setText(result.toJson());
        workExecutionService.update(workExecution);
    }

    public void finishLab(final long workId) {
        val processingWork = checkLab(workId);

        processingWork.setPhase(WorkPhase.FINISHED);
        if (processingWork.getLabResult() != null) {
            processingWork.getLabResult().setEndTime(new Timestamp(System.currentTimeMillis()));
        }

        workExecutionService.update(processingWork);
    }

    private WorkExecutionEntity checkLab(final long workId) {
        val work = workService.getWork(workId);

        if (work.getLab() == Lab.EMPTY) {
            throw new RuntimeException();
        }

        val processingWork = workExecutionService.getProcessingWork(workId);

        if (processingWork == null) {
            throw new AccessDeniedException("Access denied");
        }

        if (processingWork.getPhase() != WorkPhase.LAB) {
            throw new WorkAlreadyExistsException();
        }
        return processingWork;
    }

    private LabData runLab(final Lab lab, final long executionId, final Map<String, String> data) {
        try {
            LabStrategy labStrategy = lab.getStrategyClass().newInstance();
            return labStrategy.process(data, LAB_FILES_FOLDER_PATH, executionId + "-");

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private Path getLabFileName(final WorkExecutionEntity workExecutionEntity, final String originalFileName) {
        val name = FilenameUtils.getBaseName(originalFileName);
        val extension = FilenameUtils.getExtension(originalFileName);
        return LAB_FILES_FOLDER_PATH.resolve(workExecutionEntity.getId() + "-input-" + name + "." + extension);
    }

    public String getTemplate(final long workId) {
        return workService.getWork(workId).getLab().getTemplate();
    }

    LabData getLabData(final LabResultEntity labResultEntity, final Lab lab) {
        return new Gson().fromJson(labResultEntity.getText(), lab.getDataClass());
    }
}
