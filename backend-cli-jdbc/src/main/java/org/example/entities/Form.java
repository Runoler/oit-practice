package org.example.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Form {
    private UUID id;
    private UUID activityId;
    private String title;
    private String formText;
    private LocalDateTime createdAt;
    private boolean isComplete;

    public Form(UUID id, UUID activityId, String title, String formText, LocalDateTime createdAt, boolean isComplete) {
        this.id = id;
        this.activityId = activityId;
        this.title = title;
        this.formText = formText;
        this.createdAt = createdAt;
        this.isComplete = isComplete;
    }

    public Form(UUID activityId, String title, String formText) {
        this(UUID.randomUUID(), activityId, title, formText, LocalDateTime.now(), false);
    }

    public Form() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.isComplete = false;
    }

    public UUID getId() { return id; }
    public UUID getActivityId() { return activityId; }
    public String getTitle() { return title; }
    public String getFormText() { return formText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isComplete() { return isComplete; }

    public void setId(UUID id) { this.id = id; }
    public void setActivityId(UUID activityId) { this.activityId = activityId; }
    public void setTitle(String title) { this.title = title; }
    public void setFormText(String formText) { this.formText = formText; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setComplete(boolean complete) { isComplete = complete; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Form form = (Form) o;
        return Objects.equals(id, form.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
