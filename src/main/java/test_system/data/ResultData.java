package test_system.data;

import lombok.Data;
import test_system.entity.WorkExecutionEntity;

import java.util.concurrent.TimeUnit;

@Data
public final class ResultData {
    private int questionsAmount;
    private int correctQuestionsAmount;
    private long duration;

    public ResultData(final WorkExecutionEntity workExecutionEntity) {
        this.questionsAmount = workExecutionEntity.getQuestionsAmount();
        this.correctQuestionsAmount = workExecutionEntity.getCorrectQuestionsAmount();
        this.duration = TimeUnit.MILLISECONDS.toMinutes(workExecutionEntity.getEndTime().getTime() - workExecutionEntity.getStartTime().getTime());
    }
}
