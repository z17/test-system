package test_system.entity;

import java.util.Arrays;
import java.util.List;

public enum WorkPhase {
    THEORY("Теория", "theory"),
    TEST("Тест", "test", THEORY),
    LAB("Лабораторная работа", "lab", TEST),
    FINISHED("Завершено", "finish", TEST, LAB);

    private final String text;


    private final String url;
    private final List<WorkPhase> sourcePhases;

    public List<WorkPhase> getSourcePhases() {
        return sourcePhases;
    }

    WorkPhase(String text, String url, WorkPhase... from) {
        this.text = text;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
