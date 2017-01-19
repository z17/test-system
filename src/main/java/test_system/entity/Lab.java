package test_system.entity;

import test_system.lab.HolographyLab;
import test_system.lab.HolographyLabData;
import test_system.lab.LabData;
import test_system.lab.LabStrategy;

public enum Lab {
    EMPTY("Нет", null, null, null, null),
    HOLOGRAPHY("Голография", "lab/holography", "lab/holography-result", HolographyLab.class, HolographyLabData.class);

    Lab(final String name, final String template, final String resultTemplate, final Class<? extends LabStrategy> strategyClass, final Class<? extends LabData> resultClass) {
        this.name = name;
        this.template = template;
        this.resultTemplate = resultTemplate;
        this.strategyClass = strategyClass;
        this.dataClass = resultClass;
    }

    private final String name;
    private final String template;
    private final String resultTemplate;
    private final Class<? extends LabStrategy> strategyClass;
    private final Class<? extends LabData> dataClass;

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public String getResultTemplate() { return resultTemplate; }

    public Class<? extends LabStrategy> getStrategyClass() {
        return strategyClass;
    }

    public Class<? extends LabData> getDataClass() {
        return dataClass;
    }
}
