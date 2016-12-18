package test_system.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "test")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_id", nullable = false)
    private Long workId;


    @Column(name = "description", nullable = false)
    private String description;

    public Long getId() {
        return id;
    }

    public Long getWorkId() {
        return workId;
    }

    public String getDescription() {
        return description;
    }
}
