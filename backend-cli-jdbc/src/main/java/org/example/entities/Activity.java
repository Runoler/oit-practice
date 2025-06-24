package org.example.entities;

import java.util.Objects;
import java.util.UUID;

public class Activity {
    private UUID id;
    private String activityName;

    public Activity(UUID id, String activityName) {
        this.id = id;
        this.activityName = activityName;
    }

    public Activity(String activityName) {
        this(UUID.randomUUID(), activityName);
    }

    public Activity() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }
    public String getActivityName() { return activityName; }

    public void setId(UUID id) { this.id = id; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
