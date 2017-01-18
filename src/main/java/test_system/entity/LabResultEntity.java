package test_system.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "lab_result")
@Entity
public class LabResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "work_execution_id")
    private WorkExecutionEntity workExecution;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "start_time", insertable = false, updatable = false)
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;
}
