package test_system.data;

import com.google.gson.Gson;
import lombok.Data;
import test_system.entity.WorkExecutionEntity;
import test_system.lab.LabData;

import java.util.concurrent.TimeUnit;

@Data
public final class ResultData {
    private final int questionsAmount;
    private final int correctQuestionsAmount;
    private final long duration;
    private final LabData labData;
    private final String labResultTemplate;

    public ResultData(final WorkExecutionEntity workExecutionEntity) {
        this.questionsAmount = workExecutionEntity.getQuestionsAmount();
        this.correctQuestionsAmount = workExecutionEntity.getCorrectQuestionsAmount();
        this.duration = TimeUnit.MILLISECONDS.toMinutes(workExecutionEntity.getTestEndTime().getTime() - workExecutionEntity.getTestStartTime().getTime());

        if (workExecutionEntity.getLabResult() != null) {
            this.labData = new Gson().fromJson(workExecutionEntity.getLabResult().getText(), workExecutionEntity.getWork().getLab().getDataClass());
            this.labResultTemplate = workExecutionEntity.getWork().getLab().getResultTemplate();
        } else {
            this.labData = null;
            this.labResultTemplate = null;
        }
    }
}
