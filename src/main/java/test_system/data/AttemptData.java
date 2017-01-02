package test_system.data;

import lombok.Data;
import test_system.entity.WorkExecutionEntity;

import java.util.List;

@Data
public class AttemptData<T> {
    private final WorkExecutionEntity attempt;
    private final List<QuestionData<T>> questions;
}
