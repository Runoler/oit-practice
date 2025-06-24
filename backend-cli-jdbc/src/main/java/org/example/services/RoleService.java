package org.example.services;

import org.example.dao.RoleDAO;
import org.example.entities.Role;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoleService extends BaseService {
    private RoleDAO roleDAO;

    public RoleService(Connection connection) {
        super(connection);
        this.roleDAO = new RoleDAO(getConnection());
    }

    public Optional<Role> createRole(String roleName) {
        Role newRole = new Role(roleName);
        return roleDAO.create(newRole);
    }

    public Optional<Role> getRoleById(UUID roleId) {
        return roleDAO.findById(roleId);
    }

    public List<Role> getAllRoles() {
        return roleDAO.findAll();
    }

    public Optional<Role> updateRole(Role role) {
        return roleDAO.update(role);
    }

    public boolean deleteRole(UUID roleId) {
        return roleDAO.delete(roleId);
    }
}