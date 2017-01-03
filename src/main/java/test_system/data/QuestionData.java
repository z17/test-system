package test_system.data;

import lombok.Data;
import test_system.entity.QuestionType;

import java.util.List;

@Data
public final class QuestionData<T> {
    private final Long id;
    private final String text;
    private final QuestionType type;
    private final List<T> answers;
}
