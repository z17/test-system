package test_system.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "work_answer")
public class WorkAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_execution_id")
    private WorkExecutionEntity workExecution;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    public WorkAnswerEntity(final WorkExecutionEntity workExecution, final long answerId) {
        this.answerId = answerId;
        this.workExecution = workExecution;
    }
}
