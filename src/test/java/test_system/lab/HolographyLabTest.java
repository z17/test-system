package test_system.lab;

import lombok.val;
import org.junit.Test;
import test_system.service.LabService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

public class HolographyLabTest {
    private HolographyLab lab = new HolographyLab();

    @Test
    public void process() throws Exception {

        Map<String, String> data = new HashMap<>();
        data.put(HolographyLab.FILE_KEY, "test.bmp");
        data.put(HolographyLab.A_REPAIR_KEY, "6");
        data.put(HolographyLab.L_REPAIR_KEY, "632");
        data.put(HolographyLab.D_REPAIR_KEY, "0.058");

        data.put(HolographyLab.A_HOLO_KEY, "6");
        data.put(HolographyLab.L_HOLO_KEY, "632");
        data.put(HolographyLab.D_HOLO_KEY, "0.058");

        HolographyLabData result = lab.process(data, LabService.LAB_FILES_FOLDER_PATH, "56-");

        assertThat(result.getCorrelationCoefficient(), greaterThan(0.9));
        assertThat(result.getCorrelationCoefficient(), lessThan(1.0));
        val path = LabService.STATIC_FOLDER_PATH.toAbsolutePath().toString();
        assertThat(Files.exists(Paths.get(path + result.getPathToHolography())), is(true));
        assertThat(Files.exists(Paths.get(path + result.getPathToRestored())), is(true));
    }

}