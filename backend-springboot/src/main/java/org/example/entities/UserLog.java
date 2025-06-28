package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class UserLog {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Getter
    @Column(name = "action_description", nullable = false, updatable = false)
    private String actionDescription;

    @Getter
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private UserLog(UUID id, User user, String actionDescription, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.actionDescription = actionDescription;
        this.createdAt = createdAt;
    }

    public static UserLog createNewUserLog(User user, String actionDescription) {
        if (user == null || actionDescription == null) {
            throw new IllegalArgumentException("User or action cannot be null.");
        }
        return new UserLog(UUID.randomUUID(), user, actionDescription, LocalDateTime.now());
    }
}
