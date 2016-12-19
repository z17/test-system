package test_system.entity;

public enum Role {
    ROLE_ADMIN("admin"), ROLE_USER("user");

    final String name;

    Role(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
