package org.example.dao;

import org.example.entities.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoleDAO extends AbstractDAO<Role, UUID> {

    public RoleDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех ролей: " + e.getMessage());
        }
        return roles;
    }

    @Override
    public Optional<Role> findById(UUID id) {
        String sql = "SELECT * FROM roles WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Роль найдена.");
                    return Optional.of(mapResultSetToRole(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске роли по ID: " + e.getMessage());
        }
        System.out.println("Роль не найдена.");
        return Optional.empty();
    }

    @Override
    public Optional<Role> create(Role role) {
        String sql = "INSERT INTO roles (id, role_name) VALUES (?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, role.getId());
            ps.setString(2, role.getRoleName());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Роль успешно создана.");
                return findById(role.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании роли: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> update(Role role) {
        String sql = "UPDATE roles SET role_name = ? WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, role.getRoleName());
            ps.setObject(2, role.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Роль успешно обновлена.");
                return findById(role.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении роли: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM roles WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Роль успешно удалена.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении роли: " + e.getMessage());
        }
        return false;
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String roleName = rs.getString("role_name");
        return new Role(id, roleName);
    }
}
