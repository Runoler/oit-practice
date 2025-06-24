package org.example.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private boolean isActive;
    private int reputation;
    private String avatarUrl;
    private boolean isDeleted;

    public User(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt,
                LocalDateTime lastLoginAt, boolean isActive, int reputation, String avatarUrl, boolean isDeleted) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
        this.reputation = reputation;
        this.avatarUrl = avatarUrl;
        this.isDeleted = isDeleted;
    }

    public User(String username, String email, String passwordHash, String avatarUrl) {
        this(UUID.randomUUID(), username, email, passwordHash, LocalDateTime.now(), null, false, 0, avatarUrl, false);
    }

    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.isActive = false;
        this.reputation = 0;
        this.isDeleted = false;
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public boolean isActive() { return isActive; }
    public int getReputation() { return reputation; }
    public String getAvatarUrl() { return avatarUrl; }
    public boolean isDeleted() { return isDeleted; }

    public void setId(UUID id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public void setActive(boolean active) { isActive = active; }
    public void setReputation(int reputation) { this.reputation = reputation; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
