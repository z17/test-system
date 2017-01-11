package test_system.entity;

import lombok.Data;

import javax.persistence.*;

public enum Lab {
    EMPTY("Нет"),
    HOLOGRAPHY("Голография");

    Lab(final String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
