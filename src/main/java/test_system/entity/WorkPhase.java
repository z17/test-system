package test_system.entity;

import java.util.Arrays;
import java.util.List;

public enum  WorkPhase {
    THEORY("Теория"),
    TEST("Тест", THEORY),
//    LAB(TEST),
    FINISHED("Завершено", TEST);

    private final String text;
    private final List<WorkPhase> sourcePhases;

    public List<WorkPhase> getSourcePhases() {
        return sourcePhases;
    }

    WorkPhase(String text, WorkPhase... from)  {
        this.text = text;
        sourcePhases = Arrays.asList(from);
    }

    public static boolean isTransitionAccess(final WorkPhase source, final WorkPhase destination) {
        return source == destination
                || destination.getSourcePhases().contains(source)
                || source == null && destination.getSourcePhases().size() == 0;
    }

    public String getText() {
        return text;
    }
}
