package test_system.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuestionEntity> questions;

    public Long getId() {
        return id;
    }

    public Long getWorkId() {
        return workId;
    }

    public String getDescription() {
        return description;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }
}
