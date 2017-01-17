package test_system.entity;

import lombok.Data;

import javax.persistence.*;

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
}
