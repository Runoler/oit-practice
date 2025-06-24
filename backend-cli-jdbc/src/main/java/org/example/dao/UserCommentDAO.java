package org.example.dao;

import org.example.entities.UserComment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserCommentDAO extends AbstractDAO<UserComment, UUID> {

    public UserCommentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<UserComment> findAll() {
        List<UserComment> userComments = new ArrayList<>();
        String sql = "SELECT * FROM user_comments;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                userComments.add(mapResultSetToUserComment(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех комментариев пользователя: " + e.getMessage());
        }
        return userComments;
    }

    @Override
    public Optional<UserComment> findById(UUID id) {
        String sql = "SELECT * FROM user_comments WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Комментарий пользователя найден.");
                    return Optional.of(mapResultSetToUserComment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске комментария пользователя по ID: " + e.getMessage());
        }
        System.out.println("Комментарий пользователя не найден.");
        return Optional.empty();
    }

    @Override
    public Optional<UserComment> create(UserComment userComment) {
        String sql = "INSERT INTO user_comments (id, author_id, target_id, comment_text, reputation, created_at) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userComment.getId());
            ps.setObject(2, userComment.getAuthorId());
            ps.setObject(3, userComment.getTargetId());
            ps.setString(4, userComment.getCommentText());
            ps.setInt(5, userComment.getReputation());
            ps.setTimestamp(6, Timestamp.valueOf(userComment.getCreatedAt()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Комментарий пользователя успешно создан.");
                return findById(userComment.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании комментария пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserComment> update(UserComment userComment) {
        String sql = "UPDATE user_comments SET comment_text = ?, reputation = ? WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, userComment.getCommentText());
            ps.setInt(2, userComment.getReputation());
            ps.setObject(3, userComment.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Комментарий пользователя успешно обновлен.");
                return findById(userComment.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении комментария пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM user_comments WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Комментарий пользователя успешно удален.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении комментария пользователя: " + e.getMessage());
        }
        return false;
    }

    private UserComment mapResultSetToUserComment(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        UUID authorId = UUID.fromString(rs.getString("author_id"));
        UUID targetId = UUID.fromString(rs.getString("target_id"));
        String commentText = rs.getString("comment_text");
        int reputation = rs.getInt("reputation");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new UserComment(id, authorId, targetId, commentText, reputation, createdAt);
    }
}
