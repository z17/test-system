package test_system.entity;

import test_system.lab.HolographyLab;
import test_system.lab.LabStrategy;

public enum Lab {
    EMPTY("Нет", "", null),
    HOLOGRAPHY("Голография", "lab/holography", HolographyLab.class);

    Lab(final String name, final String template, final Class<? extends LabStrategy> strategy) {
        this.name = name;
        this.template = template;
        this.strategy = strategy;
    }

    private final String name;
    private final String template;
    private final Class<? extends LabStrategy> strategy;

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public Class<? extends LabStrategy> getStrategy() {
        return strategy;
    }
}
