package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "custom_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class User {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @Column(name = "username", nullable = false)
    private String username;

    @Getter
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Getter
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Getter
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Getter
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Getter
    @Column(name = "reputation", nullable = false)
    private int reputation;

    @Getter
    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @Getter
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private final Set<UserComment> writtenUserComments = new HashSet<>();

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    private final Set<UserComment> takenUserComments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private final Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY,
            orphanRemoval = true)
    private final Set<UserFormAssociation> userFormAssociations = new HashSet<>();

    private User(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt,
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

    public static User createNewUser(String username, String email, String passwordHash) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (email.split("@").length != 2) {
            throw new IllegalArgumentException("Wrong email pattern. Must be one \"@\" there.");
        }
        return new User(UUID.randomUUID(), username, email, passwordHash, LocalDateTime.now(),
                null, false, 0, null, false);
    }

    public void updateUsername(String username) {
        if (username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty string.");
        }
        this.username = username;
    }

    public void updateEmail(String email) {
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null.");
        }
        if (email.split("@").length != 2) {
            throw new IllegalArgumentException("Wrong email pattern. Must be one \"@\" there.");
        }
        this.email = email;
    }

    public void updatePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Wrong password.");
        }
        this.passwordHash = passwordHash;
    }

    public void login() {
        this.lastLoginAt = LocalDateTime.now();
    }
    public void activateUser() {
        this.isActive = true;
    }

    public void deactivateUser() {
        this.isActive = false;
    }

    public void earnReputation(int reputation) {
        this.reputation += reputation;
    }

    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void deleteUser() {
        this.isDeleted = true;
    }

    public void restoreUser() {
        this.isDeleted = false;
    }

    public Set<UserComment> getWrittenUserComments() {
        return Collections.unmodifiableSet(this.writtenUserComments);
    }

    public Set<UserComment> getTakenUserComments() {
        return Collections.unmodifiableSet(this.takenUserComments);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(this.roles);
    }

    public Set<UserFormAssociation> getUserFormAssociations() {
        return Collections.unmodifiableSet(this.userFormAssociations);
    }

    public void addNewComment(UserComment userComment) {
        if (userComment == null) {
            throw new IllegalArgumentException("User comment cannot be null.");
        }
        if (userComment.getAuthor() != this) {
            throw new IllegalArgumentException("This comment written by other.");
        }
        this.writtenUserComments.add(userComment);
    }

    public void removeWrittenComment(UserComment userComment) {
        if (userComment.getAuthor() != this) {
            throw new IllegalArgumentException("This comment written by other.");
        }
        this.writtenUserComments.remove(userComment);
    }

    public void takeNewComment(UserComment userComment) {
        if (userComment == null) {
            throw new IllegalArgumentException("User comment cannot be null.");
        }
        if (userComment.getTarget() != this) {
            throw new IllegalArgumentException("This comment written to other.");
        }
        this.takenUserComments.add(userComment);
    }

    public void removeTakenComment(UserComment userComment) {
        if (userComment.getTarget() != this) {
            throw new IllegalArgumentException("This comment written to other.");
        }
        this.takenUserComments.remove(userComment);
    }

    public void addRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void joinForm(UserFormAssociation userFormAssociation) {
        if (userFormAssociation == null) {
            throw new IllegalArgumentException("Association cannot be null.");
        }
        if (userFormAssociation.getUser() != this) {
            throw new IllegalArgumentException("Association doesn't contain this user.");
        }
        this.userFormAssociations.add(userFormAssociation);
    }

    public void leaveForm(UserFormAssociation userFormAssociation) {
        this.userFormAssociations.remove(userFormAssociation);
    }
}
