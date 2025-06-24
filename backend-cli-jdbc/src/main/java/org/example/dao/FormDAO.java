package org.example.dao;

import org.example.entities.Form;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FormDAO extends AbstractDAO<Form, UUID> {

    public FormDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Form> findAll() {
        List<Form> forms = new ArrayList<>();
        String sql = "SELECT * FROM forms;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                forms.add(mapResultSetToForm(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех форм: " + e.getMessage());
        }
        return forms;
    }

    @Override
    public Optional<Form> findById(UUID id) {
        String sql = "SELECT * FROM forms WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Форма найдена.");
                    return Optional.of(mapResultSetToForm(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске формы по ID: " + e.getMessage());
        }
        System.out.println("Форма не найдена.");
        return Optional.empty();
    }

    @Override
    public Optional<Form> create(Form form) {
        String sql = "INSERT INTO forms (id, activity_id, title, form_text, created_at, is_complete) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, form.getId());
            ps.setObject(2, form.getActivityId());
            ps.setString(3, form.getTitle());
            ps.setString(4, form.getFormText());
            ps.setTimestamp(5, Timestamp.valueOf(form.getCreatedAt()));
            ps.setBoolean(6, form.isComplete());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Форма успешно создана.");
                return findById(form.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании формы: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Form> update(Form form) {
        String sql = "UPDATE forms SET activity_id = ?, title = ?, form_text = ?, is_complete = ? WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, form.getActivityId());
            ps.setString(2, form.getTitle());
            ps.setString(3, form.getFormText());
            ps.setBoolean(4, form.isComplete());
            ps.setObject(5, form.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Форма успешно обновлена.");
                return findById(form.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении формы: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM forms WHERE id = ?;"; // ON DELETE CASCADE на внешнем ключе
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Форма успешно удалена.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении формы: " + e.getMessage());
        }
        return false;
    }

    Form mapResultSetToForm(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        UUID activityId = UUID.fromString(rs.getString("activity_id"));
        String title = rs.getString("title");
        String formText = rs.getString("form_text");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        boolean isComplete = rs.getBoolean("is_complete");
        return new Form(id, activityId, title, formText, createdAt, isComplete);
    }
}
