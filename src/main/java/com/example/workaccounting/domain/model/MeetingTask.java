package com.example.workaccounting.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "meeting_tasks")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class MeetingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_task_id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_meeting_id", nullable = false)
    @ToString.Exclude
    private TeamMeeting teamMeeting;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    @Setter(AccessLevel.NONE)
    private Long version;
}
