package com.example.workaccounting.domain.model;

import com.example.workaccounting.domain.enums.EvaluatorType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "milestone_evaluations")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class MilestoneEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_evaluation_id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_milestone_id", nullable = false)
    @ToString.Exclude
    private ProjectMilestone projectMilestone;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluator_type", nullable = false)
    private EvaluatorType evaluatorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_user_id", nullable = false)
    @ToString.Exclude
    private UserInfo evaluatorUser;

    @Column(name = "external_contact", nullable = false, columnDefinition = "TEXT")
    private String externalContact;

    @Column(name = "score", nullable = false)
    private BigDecimal score;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @Version
    @Column(name = "version", nullable = false)
    @Setter(AccessLevel.NONE)
    private Long version;
}
