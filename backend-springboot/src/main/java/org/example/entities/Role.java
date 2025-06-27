package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany
    private Set<User> users = new HashSet<>();

    public Role(String roleName) {
        this.id = UUID.randomUUID();
        this.roleName = roleName;
    }

    public void setRoleName(String roleName) {
        if (roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be empty.");
        }
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(this.users);
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        this.users.add(user);
    }

    public void removeUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        this.users.add(user);
    }
}
