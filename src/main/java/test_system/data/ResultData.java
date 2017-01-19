package test_system.data;

import lombok.Data;
import test_system.lab.LabData;

@Data
public final class ResultData {
    private final int questionsAmount;
    private final int correctQuestionsAmount;
    private final long testDuration;
    private final LabData labData;
    private final String labResultTemplate;
}
