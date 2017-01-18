package test_system.entity;

import test_system.lab.HolographyLab;
import test_system.lab.HolographyLabResult;
import test_system.lab.LabResult;
import test_system.lab.LabStrategy;

public enum Lab {
    EMPTY("Нет", "", null, null),
    HOLOGRAPHY("Голография", "lab/holography", HolographyLab.class, HolographyLabResult.class);

    Lab(final String name, final String template, final Class<? extends LabStrategy> strategyClass, final Class<? extends LabResult> resultClass) {
        this.name = name;
        this.template = template;
        this.strategyClass = strategyClass;
        this.resultClass = resultClass;
    }

    private final String name;
    private final String template;
    private final Class<? extends LabStrategy> strategyClass;
    private final Class<? extends LabResult> resultClass;

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public Class<? extends LabStrategy> getStrategyClass() {
        return strategyClass;
    }

    public Class<? extends LabResult> getResultClass() {
        return resultClass;
    }
}
