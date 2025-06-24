package org.example.entities;

import java.util.Objects;
import java.util.UUID;

public class Role {
    private UUID id;
    private String roleName;

    public Role(UUID id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Role(String roleName) {
        this(UUID.randomUUID(), roleName);
    }

    public Role() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }
    public String getRoleName() { return roleName; }

    public void setId(UUID id) { this.id = id; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}