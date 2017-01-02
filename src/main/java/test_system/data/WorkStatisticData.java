package test_system.data;

import lombok.Data;
import test_system.entity.WorkExecutionEntity;

import java.util.List;

@Data
public final class WorkStatisticData {
    private final String name;
    private final int correctPercent;
    private final int time;
    private final int attempts;
    private final List<QuestionData<StatisticAnswerData>> questions;

    private final List<WorkExecutionEntity> attemptsList;
}
