package test_system.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "work_execution")
@Entity
public class WorkExecutionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_id")
    private WorkEntity work;

    @Column(name = "phase")
    @Enumerated(value = EnumType.STRING)
    private WorkPhase phase;

    @Column(name = "start_time", insertable = false, updatable = false)
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "correct_questions")
    private int correctQuestionsAmount;

    @Column(name = "questions")
    private int questionsAmount;

}
