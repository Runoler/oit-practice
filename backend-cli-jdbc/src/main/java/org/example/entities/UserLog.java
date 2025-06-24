package org.example.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class UserLog {
    private UUID id;
    private UUID userId;
    private String actionDesc;
    private LocalDateTime actionDate;

    public UserLog(UUID id, UUID userId, String actionDesc, LocalDateTime actionDate) {
        this.id = id;
        this.userId = userId;
        this.actionDesc = actionDesc;
        this.actionDate = actionDate;
    }

    public UserLog(UUID userId, String actionDesc) {
        this(UUID.randomUUID(), userId, actionDesc, LocalDateTime.now());
    }

    public UserLog() {
        this.id = UUID.randomUUID();
        this.actionDate = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getActionDesc() { return actionDesc; }
    public LocalDateTime getActionDate() { return actionDate; }

    public void setId(UUID id) { this.id = id; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setActionDesc(String actionDesc) { this.actionDesc = actionDesc; }
    public void setActionDate(LocalDateTime actionDate) { this.actionDate = actionDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLog userLog = (UserLog) o;
        return Objects.equals(id, userLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
