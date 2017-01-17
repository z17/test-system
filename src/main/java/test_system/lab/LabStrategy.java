package test_system.lab;

import java.nio.file.Path;
import java.util.Map;

public interface LabStrategy {
    LabResult process(Map<String, String> data, Path pathToOutFolder, String outPrefixName);
}