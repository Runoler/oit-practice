package org.example.dao;

import org.example.entities.UserFormAssociation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFormAssociationDAO extends AbstractDAO<UserFormAssociation, UUID[]> {

    public UserFormAssociationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<UserFormAssociation> findAll() {
        List<UserFormAssociation> associations = new ArrayList<>();
        String sql = "SELECT * FROM users_forms;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                associations.add(mapResultSetToUserFormAssociation(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех ассоциаций пользователь-форма: " + e.getMessage());
        }
        return associations;
    }

    @Override
    public Optional<UserFormAssociation> findById(UUID[] ids) {
        if (ids == null || ids.length != 2) {
            System.err.println("[ERROR] Для findById ассоциации пользователь-форма требуются user_id и form_id.");
            return Optional.empty();
        }
        UUID userId = ids[0];
        UUID formId = ids[1];

        String sql = "SELECT * FROM users_forms WHERE user_id = ? AND form_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ps.setObject(2, formId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Ассоциация пользователь-форма найдена.");
                    return Optional.of(mapResultSetToUserFormAssociation(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске ассоциации пользователь-форма по ID: " + e.getMessage());
        }
        System.out.println("Ассоциация пользователь-форма не найдена.");
        return Optional.empty();
    }

    @Override
    public Optional<UserFormAssociation> create(UserFormAssociation association) {
        String sql = "INSERT INTO users_forms (user_id, form_id, is_leader) VALUES (?, ?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, association.getUserId());
            ps.setObject(2, association.getFormId());
            ps.setBoolean(3, association.isLeader());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Ассоциация пользователь-форма успешно создана.");
                return Optional.of(association);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании ассоциации пользователь-форма: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserFormAssociation> update(UserFormAssociation association) {
        String sql = "UPDATE users_forms SET is_leader = ? WHERE user_id = ? AND form_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setBoolean(1, association.isLeader());
            ps.setObject(2, association.getUserId());
            ps.setObject(3, association.getFormId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Ассоциация пользователь-форма успешно обновлена.");
                return findById(new UUID[]{association.getUserId(), association.getFormId()});
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении ассоциации пользователь-форма: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID[] ids) {
        if (ids == null || ids.length != 2) {
            System.err.println("[ERROR] Для удаления ассоциации пользователь-форма требуются user_id и form_id.");
            return false;
        }
        UUID userId = ids[0];
        UUID formId = ids[1];

        String sql = "DELETE FROM users_forms WHERE user_id = ? AND form_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ps.setObject(2, formId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Ассоциация пользователь-форма успешно удалена.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении ассоциации пользователь-форма: " + e.getMessage());
        }
        return false;
    }

    private UserFormAssociation mapResultSetToUserFormAssociation(ResultSet rs) throws SQLException {
        UUID userId = UUID.fromString(rs.getString("user_id"));
        UUID formId = UUID.fromString(rs.getString("form_id"));
        boolean isLeader = rs.getBoolean("is_leader");
        return new UserFormAssociation(userId, formId, isLeader);
    }
}
