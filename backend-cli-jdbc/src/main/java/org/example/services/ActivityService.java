package org.example.services;

import org.example.dao.ActivityDAO;
import org.example.entities.Activity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ActivityService extends BaseService {
    private ActivityDAO activityDAO;

    public ActivityService(Connection connection) {
        super(connection);
        this.activityDAO = new ActivityDAO(getConnection());
    }

    public Optional<Activity> createActivity(String activityName) {
        Activity newActivity = new Activity(activityName);
        return activityDAO.create(newActivity);
    }

    public Optional<Activity> getActivityById(UUID activityId) {
        return activityDAO.findById(activityId);
    }

    public List<Activity> getAllActivities() {
        return activityDAO.findAll();
    }

    public Optional<Activity> updateActivity(Activity activity) {
        return activityDAO.update(activity);
    }

    public boolean deleteActivity(UUID activityId) {
        return activityDAO.delete(activityId);
    }
}
