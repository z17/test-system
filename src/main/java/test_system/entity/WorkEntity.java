package test_system.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="work")
public class WorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name="lab")
    @Enumerated(EnumType.STRING)
    private Lab lab;
}
