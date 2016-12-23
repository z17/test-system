package test_system.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import test_system.entity.QuestionEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionData {
    private QuestionEntity question;
    private Long answerId;
    private Long questionId;
}
