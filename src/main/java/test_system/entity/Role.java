package test_system.entity;

public enum Role {
    ROLE_ADMIN("admin"), ROLE_USER("user"), ROLE_ANONYMOUS("anonym");

    final String name;

    Role(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
