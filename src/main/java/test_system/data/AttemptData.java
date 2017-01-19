package test_system.data;

import lombok.Data;
import test_system.entity.WorkExecutionEntity;
import test_system.lab.LabData;

import java.util.List;

@Data
public class AttemptData<T> {
    private final WorkExecutionEntity attempt;
    private final List<QuestionData<T>> questions;
    private final LabData labData;
    private final String labResultTemplate;
}
