package org.example.services;

import org.example.dao.UserDAO;
import org.example.dao.UserLogDAO;
import org.example.entities.UserLog;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserLogService extends BaseService {
    private UserLogDAO userLogDAO;
    private UserDAO userDAO;

    public UserLogService(Connection connection) {
        super(connection);
        this.userLogDAO = new UserLogDAO(getConnection());
        this.userDAO = new UserDAO(getConnection());
    }

    public Optional<UserLog> createLog(UUID userId, String actionDescription) {
        if (!userDAO.findById(userId).isPresent()) {
            System.err.println("[ERROR] Невозможно создать лог: Пользователь с ID " + userId + " не существует.");
            return Optional.empty();
        }
        UserLog newLog = new UserLog(userId, actionDescription);
        return userLogDAO.create(newLog);
    }

    public Optional<UserLog> getLogById(UUID logId) {
        return userLogDAO.findById(logId);
    }

    public List<UserLog> getAllUserLogs() {
        return userLogDAO.findAll();
    }

    public Optional<UserLog> updateLog(UserLog log) {
        if (!userDAO.findById(log.getUserId()).isPresent()) {
            System.err.println("[ERROR] Невозможно обновить лог: Пользователь с ID " + log.getUserId() + " не существует.");
            return Optional.empty();
        }
        return userLogDAO.update(log);
    }

    public boolean deleteLog(UUID logId) {
        return userLogDAO.delete(logId);
    }
}
