package test_system.data;

import lombok.Data;
import test_system.entity.Lab;

import java.util.List;

@Data
public class WorkCreateData {
    final Long id;
    final String name;
    final String description;
    final String theory;
    final String testDescription;
    final List<QuestionData<AnswerData>> questions;
    final Lab lab;
}
