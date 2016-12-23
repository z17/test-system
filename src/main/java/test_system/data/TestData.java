package test_system.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import test_system.entity.TestEntity;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestData {
    private String description;
    private List<QuestionData> questions;
    private long workId;

    public TestData(final TestEntity testEntity) {
        description = testEntity.getDescription();
        workId = testEntity.getWorkId();

        questions = testEntity.getQuestions().stream()
                .map(t -> {
                    QuestionData question = new QuestionData();
                    question.setQuestion(t);
                    question.setAnswerId(null);
                    question.setQuestionId(t.getId());
                    return question;
                }).collect(Collectors.toList());
    }
}
