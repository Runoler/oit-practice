package org.example.dao;

import org.example.entities.UserLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserLogDAO extends AbstractDAO<UserLog, UUID> {

    public UserLogDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<UserLog> findAll() {
        List<UserLog> userLogs = new ArrayList<>();
        String sql = "SELECT * FROM user_logs;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userLogs.add(mapResultSetToUserLog(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех логов пользователя: " + e.getMessage());
        }
        return userLogs;
    }

    @Override
    public Optional<UserLog> findById(UUID id) {
        String sql = "SELECT * FROM user_logs WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Лог пользователя найден.");
                    return Optional.of(mapResultSetToUserLog(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске лога пользователя по ID: " + e.getMessage());
        }
        System.out.println("Лог пользователя не найден.");
        return Optional.empty();
    }

    @Override
    public Optional<UserLog> create(UserLog userLog) {
        String sql = "INSERT INTO user_logs (id, user_id, action_desc, action_date) VALUES (?, ?, ?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userLog.getId());
            ps.setObject(2, userLog.getUserId());
            ps.setString(3, userLog.getActionDesc());
            ps.setTimestamp(4, Timestamp.valueOf(userLog.getActionDate()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Лог пользователя успешно создан.");
                return findById(userLog.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании лога пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserLog> update(UserLog userLog) {
        String sql = "UPDATE user_logs SET action_desc = ?, action_date = ? WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, userLog.getActionDesc());
            ps.setTimestamp(2, Timestamp.valueOf(userLog.getActionDate()));
            ps.setObject(3, userLog.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Лог пользователя успешно обновлен.");
                return findById(userLog.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении лога пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM user_logs WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Лог пользователя успешно удален.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении лога пользователя: " + e.getMessage());
        }
        return false;
    }

    private UserLog mapResultSetToUserLog(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        UUID userId = UUID.fromString(rs.getString("user_id"));
        String actionDesc = rs.getString("action_desc");
        LocalDateTime actionDate = rs.getTimestamp("action_date").toLocalDateTime();
        return new UserLog(id, userId, actionDesc, actionDate);
    }
}
