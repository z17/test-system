package test_system.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "theory")
public class TheoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_id", nullable = false)
    private Long workId;

    @Column(name = "text", nullable = false)
    private String text;

    public Long getId() {
        return id;
    }

    public Long getWorkId() {
        return workId;
    }

    public String getText() {
        return text;
    }
}
