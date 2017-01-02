package test_system.data;

import lombok.Data;

import java.util.List;

@Data
public class WorkCreateData {
    final String title;
    final String description;
    final String theory;
    final String testDescription;
    final List<QuestionData<AnswerData>> questions;

}
