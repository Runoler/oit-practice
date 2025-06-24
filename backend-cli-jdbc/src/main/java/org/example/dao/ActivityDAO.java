package org.example.dao;

import org.example.entities.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ActivityDAO extends AbstractDAO<Activity, UUID> {

    public ActivityDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Activity> findAll() {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске всех активностей: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public Optional<Activity> findById(UUID id) {
        String sql = "SELECT * FROM activities WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Активность найдена.");
                    return Optional.of(mapResultSetToActivity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при поиске активности по ID: " + e.getMessage());
        }
        System.out.println("Активность не найдена.");
        return Optional.empty();
    }

    @Override
    public Optional<Activity> create(Activity activity) {
        String sql = "INSERT INTO activities (id, activity_name) VALUES (?, ?);";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, activity.getId());
            ps.setString(2, activity.getActivityName());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Активность успешно создана.");
                return findById(activity.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при создании активности: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Activity> update(Activity activity) {
        String sql = "UPDATE activities SET activity_name = ? WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, activity.getActivityName());
            ps.setObject(2, activity.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Активность успешно обновлена.");
                return findById(activity.getId());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при обновлении активности: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM activities WHERE id = ?;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Активность успешно удалена.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка при удалении активности: " + e.getMessage());
        }
        return false;
    }

    private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String activityName = rs.getString("activity_name");
        return new Activity(id, activityName);
    }
}
