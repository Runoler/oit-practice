package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "activity")
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @Column(name = "activity_name")
    private String activityName;

    public Activity(String activityName) {
        this.id = UUID.randomUUID();
        this.activityName = activityName;
    }

    public void setActivityName(String activityName) {
        if (activityName.trim().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be empty.");
        }
        this.activityName = activityName;
    }
}
