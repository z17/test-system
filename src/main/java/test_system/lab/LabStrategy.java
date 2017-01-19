package test_system.lab;

import java.nio.file.Path;
import java.util.Map;

public interface LabStrategy {
    LabData process(Map<String, String> data, Path pathToOutFolder, String outPrefixName);
}