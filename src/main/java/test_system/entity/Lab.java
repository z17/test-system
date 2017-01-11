package test_system.entity;

public enum Lab {
    EMPTY("Нет", ""),
    HOLOGRAPHY("Голография", "lab/holography");

    Lab(final String name, final String template) {
        this.name = name;
        this.template = template;
    }

    private final String name;
    private final String template;

    public String getName() {
        return name;
    }
    public String getTemplate() {
        return template;
    }
}
