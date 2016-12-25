package test_system.data;

import lombok.Data;

import java.util.List;

@Data
public final class WorkStatisticData {
    private final String name;
    private final int correctPercent;
    private final int time;
    private final int attempts;
    private final List<WorkStatisticQuestionData> questions;
}
