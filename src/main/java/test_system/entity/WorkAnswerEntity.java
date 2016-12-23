package test_system.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "work_answer")
public class WorkAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_id")
    private Long answerId;

    public WorkAnswerEntity(final long answerId) {
        this.answerId = answerId;
    }
}
