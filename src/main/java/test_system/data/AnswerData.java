package test_system.data;

import lombok.Data;

@Data
public class AnswerData {
    private final Long id;
    private final String text;
    private final boolean correct;
}
