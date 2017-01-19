package test_system.lab;

import com.google.gson.Gson;
import lombok.Data;
import test_system.service.LabService;

import java.nio.file.Path;

@Data
public class HolographyLabData implements LabData {
    private final String pathToInputImage;

    private final double holoL;
    private final double holoA;
    private final double holoD;

    private final double repairL;
    private final double repairA;
    private final double repairD;

    private final String pathToHolography;
    private final String pathToRestored;
    private final double correlationCoefficient;

    public HolographyLabData(
            final Path pathToInputImage,
            final double holoL,
            final double holoA,
            final double holoD,
            final double repairL,
            final double repairA,
            final double repairD,
            final Path pathToHolography,
            final Path pathToRestored,
            final double correlationCoefficient) {
        this.pathToInputImage = "/" + LabService.LAB_FILES_FOLDER + "/" + pathToInputImage.getFileName();
        this.holoL = holoL;
        this.holoA = holoA;
        this.holoD = holoD;
        this.repairL = repairL;
        this.repairA = repairA;
        this.repairD = repairD;
        this.pathToHolography = "/" + LabService.LAB_FILES_FOLDER + "/" + pathToHolography.getFileName();
        this.pathToRestored = "/" + LabService.LAB_FILES_FOLDER + "/" + pathToRestored.getFileName();
        this.correlationCoefficient = correlationCoefficient;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
