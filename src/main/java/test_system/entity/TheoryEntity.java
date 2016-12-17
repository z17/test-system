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

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "text", nullable = false)
    private String text;
}
