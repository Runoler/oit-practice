package org.example.dao;

import org.example.entities.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagDAO extends AbstractDAO<Tag, UUID> {

    public TagDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tags;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tags.add(mapResultSetToTag(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех тегов: " + e.getMessage());
        }
        return tags;
    }

    @Override
    public Optional<Tag> findById(UUID id) {
        String sql = "SELECT * FROM tags WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Тег найден.");
                    return Optional.of(mapResultSetToTag(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске тега по ID: " + e.getMessage());
        }
        System.out.println("Тег не найден.");
        return Optional.empty();
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        String sql = "INSERT INTO tags (id, tag_name) VALUES (?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, tag.getId());
            ps.setString(2, tag.getTagName());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Тег успешно создан.");
                return findById(tag.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании тега: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        String sql = "UPDATE tags SET tag_name = ? WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, tag.getTagName());
            ps.setObject(2, tag.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Тег успешно обновлен.");
                return findById(tag.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении тега: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM tags WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Тег успешно удален.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении тега: " + e.getMessage());
        }
        return false;
    }

    private Tag mapResultSetToTag(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String tagName = rs.getString("tag_name");
        return new Tag(id, tagName);
    }
}
