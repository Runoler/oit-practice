package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Role {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    private Role(UUID id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public static Role createNewRole(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty.");
        }
        return new Role(UUID.randomUUID(), roleName);
    }

    public void updateRoleName(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty.");
        }
        this.roleName = roleName;
    }
}
