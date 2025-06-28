package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "activity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Activity {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @Column(name = "activity_name", nullable = false, unique = true)
    private String activityName;

    private Activity(UUID id, String activityName) {
        this.id = id;
        this.activityName = activityName;
    }

    public static Activity createNewActivity(String activityName) {
        if (activityName == null || activityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty.");
        }
        return new Activity(UUID.randomUUID(), activityName.trim());
    }

    public void updateActivityName(String activityName) {
        if (activityName == null || activityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty.");
        }
        this.activityName = activityName;
    }
}
