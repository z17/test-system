package test_system.lab;

import com.google.gson.Gson;
import lombok.Data;

import java.nio.file.Path;

@Data
public class HolographyLabResult implements LabResult {
    private final Path pathToHolography;
    private final Path pathToRestored;
    private final double correlationCoefficient;

    public HolographyLabResult(Path pathToHolography, Path pathToRestored, double correlationCoefficient) {
        this.pathToHolography = pathToHolography;
        this.pathToRestored = pathToRestored;
        this.correlationCoefficient = correlationCoefficient;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
