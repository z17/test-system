package test_system.data;

import lombok.Data;

@Data
public class AttemptAnswerData {
    private final String text;
    private final boolean correct;
    private final boolean selected;
}
