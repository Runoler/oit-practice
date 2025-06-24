package org.example.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class UserComment {
    private UUID id;
    private UUID authorId;
    private UUID targetId;
    private String commentText;
    private int reputation;
    private LocalDateTime createdAt;

    public UserComment(UUID id, UUID authorId, UUID targetId, String commentText, int reputation, LocalDateTime createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.targetId = targetId;
        this.commentText = commentText;
        setReputation(reputation);
        this.createdAt = createdAt;
    }

    public UserComment(UUID authorId, UUID targetId, String commentText, int reputation) {
        this(UUID.randomUUID(), authorId, targetId, commentText, reputation, LocalDateTime.now());
    }

    public UserComment() {
        this.id = UUID.randomUUID();
        this.reputation = 0;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getAuthorId() { return authorId; }
    public UUID getTargetId() { return targetId; }
    public String getCommentText() { return commentText; }
    public int getReputation() { return reputation; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public void setTargetId(UUID targetId) { this.targetId = targetId; }
    public void setCommentText(String commentText) { this.commentText = commentText; }
    public void setReputation(int reputation) {
        if (reputation >= -1 && reputation <= 1) {
            this.reputation = reputation;
        } else {
            this.reputation = 0;
        }
    }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserComment that = (UserComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
