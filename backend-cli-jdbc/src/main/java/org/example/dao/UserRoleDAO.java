package org.example.dao;

import org.example.entities.Role;
import org.example.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRoleDAO {
    private Connection dbConnection;
    private UserDAO userDAO; // Для получения объектов User

    public UserRoleDAO(Connection connection, UserDAO userDAO) { // Удален RoleDAO из конструктора, так как он не используется напрямую для маппинга
        this.dbConnection = connection;
        this.userDAO = userDAO;
    }

    // Присвоить роль пользователю
    public boolean addRoleToUser(UUID userId, UUID roleId) {
        String sql = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ps.setObject(2, roleId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Роль успешно присвоена пользователю.");
                return true;
            }
        } catch (SQLException e) {
            // Игнорируем ошибку уникальности, если роль уже присвоена
            if (e.getMessage() != null && e.getMessage().contains("duplicate key value")) {
                System.out.println("Роль уже присвоена этому пользователю.");
            } else {
                System.err.println("[ERROR] Ошибка при присвоении роли пользователю: " + e.getMessage());
            }
        }
        return false;
    }

    // Удалить роль у пользователя
    public boolean removeRoleFromUser(UUID userId, UUID roleId) {
        String sql = "DELETE FROM users_roles WHERE user_id = ? AND role_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ps.setObject(2, roleId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Роль успешно удалена у пользователя.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении роли у пользователя: " + e.getMessage());
        }
        return false;
    }

    // Найти все роли, присвоенные пользователю
    public List<Role> findRolesByUserId(UUID userId) {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.id, r.role_name FROM roles r JOIN users_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(new Role(UUID.fromString(rs.getString("id")), rs.getString("role_name")));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске ролей пользователя: " + e.getMessage());
        }
        return roles;
    }

    // Найти всех пользователей с определенной ролью
    public List<User> findUsersByRoleId(UUID roleId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, u.password_hash, u.created_at, u.last_login_at, u.is_active, u.reputation, u.avatar_url, u.is_deleted FROM users u JOIN users_roles ur ON u.id = ur.user_id WHERE ur.role_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(userDAO.mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске пользователей по роли: " + e.getMessage());
        }
        return users;
    }
}
