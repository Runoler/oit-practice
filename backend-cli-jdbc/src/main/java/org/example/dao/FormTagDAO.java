package org.example.dao;

import org.example.entities.Form;
import org.example.entities.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormTagDAO {
    private Connection dbConnection;
    private FormDAO formDAO;
    private TagDAO tagDAO;

    public FormTagDAO(Connection connection, FormDAO formDAO, TagDAO tagDAO) {
        this.dbConnection = connection;
        this.formDAO = formDAO;
        this.tagDAO = tagDAO;
    }

    public boolean addTagToForm(UUID formId, UUID tagId) {
        String sql = "INSERT INTO forms_tags (form_id, tag_id) VALUES (?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, formId);
            ps.setObject(2, tagId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Тег успешно присвоен форме.");
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("duplicate key value")) {
                System.out.println("Тег уже присвоен этой форме.");
            } else {
                System.err.println("[ERROR] Ошибка при присвоении тега форме: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean removeTagFromForm(UUID formId, UUID tagId) {
        String sql = "DELETE FROM forms_tags WHERE form_id = ? AND tag_id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, formId);
            ps.setObject(2, tagId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Тег успешно удален у формы.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении тега у формы: " + e.getMessage());
        }
        return false;
    }

public List<Tag> findTagsByFormId(UUID formId) {
    List<Tag> tags = new ArrayList<>();
    String sql = "SELECT t.id, t.tag_name FROM tags t JOIN forms_tags ft ON t.id = ft.tag_id WHERE ft.form_id = ?;";
    try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
        ps.setObject(1, formId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tags.add(new Tag(UUID.fromString(rs.getString("id")), rs.getString("tag_name")));
            }
        }
    } catch (SQLException e) {
        System.err.println("[ERROR] Ошибка при поиске тегов формы: " + e.getMessage());
    }
    return tags;
}

public List<Form> findFormsByTagId(UUID tagId) {
    List<Form> forms = new ArrayList<>();
    String sql = "SELECT f.id, f.activity_id, f.title, f.form_text, f.created_at, f.is_complete FROM forms f JOIN forms_tags ft ON f.id = ft.form_id WHERE ft.tag_id = ?;";
    try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
        ps.setObject(1, tagId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                forms.add(formDAO.mapResultSetToForm(rs));
            }
        }
    } catch (SQLException e) {
        System.err.println("[ERROR] Ошибка при поиске форм по тегу: " + e.getMessage());
    }
    return forms;
}
}
