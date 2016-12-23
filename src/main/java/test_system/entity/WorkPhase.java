package test_system.entity;

import java.util.Arrays;
import java.util.List;

public enum  WorkPhase {
    THEORY(),
    TEST(THEORY),
//    LAB(TEST),
    FINISHED(TEST);

    private final List<WorkPhase> sourcePhases;

    public List<WorkPhase> getSourcePhases() {
        return sourcePhases;
    }

    WorkPhase(WorkPhase... from) {
        sourcePhases = Arrays.asList(from);
    }

    public static boolean isTransitionAccess(final WorkPhase source, final WorkPhase destination) {
        return source == destination
                || destination.getSourcePhases().contains(source)
                || source == null && destination.getSourcePhases().size() == 0;

    }
}
