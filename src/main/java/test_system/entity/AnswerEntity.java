package test_system.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "answer")
@Entity
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "correct", nullable = false)
    private boolean isCorrect;

    public Long getId() {
        return id;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}