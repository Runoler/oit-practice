package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "custom_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @Column(name = "username")
    private String username;

    @Getter
    @Column(name = "email")
    private String email;

    @Getter
    @Setter
    @Column(name = "password_hash")
    private String passwordHash;

    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Getter
    @Setter
    @Column(name = "is_active")
    private boolean isActive;

    @Getter
    @Setter
    @Column(name = "reputation")
    private int reputation;

    @Getter
    @Setter
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Getter
    @Setter
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Getter
    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<UserComment> writtenUserComments = new HashSet<>();

    @Getter
    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    private Set<UserComment> takenUserComments = new HashSet<>();

    @Getter
    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    @Getter
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Set<UserFormAssociation> userFormAssociations = new HashSet<>();

    public User(String username, String email, String passwordHash) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = null;
        this.isActive = false;
        this.reputation = 0;
        this.avatarUrl = null;
        this.isDeleted = false;
    }

    public void setUsername(String username) {
        if (username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty string.");
        }
        this.username = username;
    }

    public void setEmail(String email) {
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null.");
        }
        if (email.split("@").length != 2) {
            throw new IllegalArgumentException("Wrong email pattern. Must be one \"@\" there.");
        }
        this.email = email;
    }
}
