package test_system.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "question")
@Entity
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private TestEntity test;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnswerEntity> answers;

    public Long getId() {
        return id;
    }

    public TestEntity getTest() {
        return test;
    }

    public String getText() {
        return text;
    }

    public QuestionType getType() {
        return type;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }
}