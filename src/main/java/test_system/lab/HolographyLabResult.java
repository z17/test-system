package test_system.lab;

import com.google.gson.Gson;
import lombok.Data;
import test_system.service.LabService;

import java.nio.file.Path;

@Data
public class HolographyLabResult implements LabResult {
    private final String pathToHolography;
    private final String pathToRestored;
    private final double correlationCoefficient;

    public HolographyLabResult(Path pathToHolography, Path pathToRestored, double correlationCoefficient) {
        this.pathToHolography = "/" + LabService.LAB_FILES_FOLDER + "/" + pathToHolography.getFileName();
        this.pathToRestored = "/" + LabService.LAB_FILES_FOLDER + "/" + pathToRestored.getFileName();
        this.correlationCoefficient = correlationCoefficient;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
