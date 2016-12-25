package test_system.data;

import lombok.Data;

@Data
public final class WorkStatisticAnswerData {
    private final String text;
    private final boolean correct;
    private final int percent;
}
