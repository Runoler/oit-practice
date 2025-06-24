package org.example.dao;

import org.example.entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserDAO extends AbstractDAO<User, UUID> {

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<User> findAll() {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users;";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                userList.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Произошла ошибка при поиске всех пользователей: " + e.getMessage());
        }
        return userList;
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = "SELECT * FROM users WHERE id=?;";

        // Использование try-with-resources
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Пользователь найден.");
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Произошла ошибка при поиске пользователя по ID: " + e.getMessage());
        }
        System.out.println("Пользователь не найден.");
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User user) {
        String sql = "UPDATE users SET " +
                "username=?, " +
                "email=?, " +
                "password_hash=?, " +
                "last_login_at=?, " +
                "is_active=?, " +
                "reputation=?, " +
                "avatar_url=?, " +
                "is_deleted=? " +
                "WHERE id=?;";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setTimestamp(4, user.getLastLoginAt() != null ? Timestamp.valueOf(user.getLastLoginAt()) : null);
            preparedStatement.setBoolean(5, user.isActive());
            preparedStatement.setInt(6, user.getReputation());
            preparedStatement.setString(7, user.getAvatarUrl());
            preparedStatement.setBoolean(8, user.isDeleted());
            preparedStatement.setObject(9, user.getId());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Пользователь успешно обновлен.");
                return findById(user.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Произошла ошибка при обновлении пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> create(User user) {
        String sql = "INSERT INTO users (id, username, email, password_hash, created_at, last_login_at, is_active, reputation, avatar_url, is_deleted) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {
            preparedStatement.setObject(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
            preparedStatement.setTimestamp(6, user.getLastLoginAt() != null ? Timestamp.valueOf(user.getLastLoginAt()) : null);
            preparedStatement.setBoolean(7, user.isActive());
            preparedStatement.setInt(8, user.getReputation());
            preparedStatement.setString(9, user.getAvatarUrl());
            preparedStatement.setBoolean(10, user.isDeleted());

            int createdRows = preparedStatement.executeUpdate();
            if (createdRows > 0) {
                System.out.println("Пользователь успешно создан.");
                return findById(user.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Произошла ошибка при создании пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "UPDATE users SET is_deleted=true WHERE id=?;";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Пользователь успешно помечен как удаленный.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Произошла ошибка при удалении пользователя: " + e.getMessage());
        }
        return false;
    }

    User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        UUID userId = UUID.fromString(resultSet.getString("id"));
        String userUsername = resultSet.getString("username");
        String userEmail = resultSet.getString("email");
        String userPasswordHash = resultSet.getString("password_hash");
        LocalDateTime userCreatedAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime userLastLoginAt = (resultSet.getTimestamp("last_login_at") == null
                ? null
                : resultSet.getTimestamp("last_login_at").toLocalDateTime());
        boolean userIsActive = resultSet.getBoolean("is_active");
        int userReputation = resultSet.getInt("reputation");
        String userAvatarURL = resultSet.getString("avatar_url");
        boolean userIsDeleted = resultSet.getBoolean("is_deleted");

        return new User(userId, userUsername, userEmail, userPasswordHash, userCreatedAt,
                userLastLoginAt, userIsActive, userReputation, userAvatarURL, userIsDeleted);
    }
}
