package org.example.services;

import org.example.dao.RoleDAO;
import org.example.dao.UserDAO;
import org.example.dao.UserLogDAO;
import org.example.dao.UserRoleDAO;
import org.example.entities.Role;
import org.example.entities.User;
import org.example.entities.UserLog;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService extends BaseService {
    private UserDAO userDAO;
    private RoleService roleService;
    private UserRoleDAO userRoleDAO;
    private UserLogService userLogService;

    public UserService(Connection connection) {
        super(connection);
        this.userDAO = new UserDAO(getConnection());
        this.roleService = new RoleService(getConnection());
        this.userRoleDAO = new UserRoleDAO(getConnection(), userDAO);
        this.userLogService = new UserLogService(getConnection());
    }

    public Optional<User> registerUser(String username, String email, String passwordHash, String avatarUrl) {
        User newUser = new User(username, email, passwordHash, avatarUrl);
        Optional<User> createdUser = userDAO.create(newUser);
        if (createdUser.isPresent()) {
            userLogService.createLog(createdUser.get().getId(), "Зарегистрирован новый пользователь.");
        }
        return createdUser;
    }

    public Optional<User> loginUser(String email, String passwordHash) {
        List<User> allUsers = userDAO.findAll();
        Optional<User> userOptional = allUsers.stream()
                .filter(u -> u.getEmail().equals(email) && u.getPasswordHash().equals(passwordHash))
                .findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastLoginAt(LocalDateTime.now());
            user.setActive(true);
            userDAO.update(user);
            userLogService.createLog(user.getId(), "Пользователь вошел в систему.");
            System.out.println("Пользователь " + user.getUsername() + " успешно вошел.");
        } else {
            System.out.println("Неверный email или пароль.");
        }
        return userOptional;
    }

    public Optional<User> getUserById(UUID userId) {
        return userDAO.findById(userId);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public Optional<User> updateUser(User user) {
        Optional<User> updated = userDAO.update(user);
        if (updated.isPresent()) {
            userLogService.createLog(updated.get().getId(), "Данные пользователя обновлены.");
        }
        return updated;
    }

    public boolean deleteUser(UUID userId) {
        Optional<User> userToDelete = userDAO.findById(userId);
        if (userToDelete.isPresent()) {
            userToDelete.get().setDeleted(true);
            userDAO.update(userToDelete.get());
            userLogService.createLog(userId, "Пользователь помечен как удаленный (мягкое удаление).");
            System.out.println("Пользователь " + userId + " помечен как удаленный.");
            return true;
        }
        System.out.println("Пользователь для удаления не найден.");
        return false;
    }

    public boolean assignRoleToUser(UUID userId, UUID roleId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Role> role = roleService.getRoleById(roleId);

        if (user.isPresent() && role.isPresent()) {
            boolean success = userRoleDAO.addRoleToUser(userId, roleId);
            if (success) {
                userLogService.createLog(userId, "Роль " + role.get().getRoleName() + " присвоена.");
            }
            return success;
        }
        System.out.println("Пользователь или роль не найдены для присвоения.");
        return false;
    }

    public boolean removeRoleFromUser(UUID userId, UUID roleId) {
        Optional<User> user = userDAO.findById(userId);
        Optional<Role> role = roleService.getRoleById(roleId);

        if (user.isPresent() && role.isPresent()) {
            boolean success = userRoleDAO.removeRoleFromUser(userId, roleId);
            if (success) {
                userLogService.createLog(userId, "Роль " + role.get().getRoleName() + " удалена.");
            }
            return success;
        }
        System.out.println("Пользователь или роль не найдены для удаления.");
        return false;
    }

    public List<Role> getUserRoles(UUID userId) {
        Optional<User> user = userDAO.findById(userId);
        if (user.isPresent()) {
            return userRoleDAO.findRolesByUserId(userId);
        }
        System.out.println("Пользователь не найден для получения ролей.");
        return new ArrayList<>();
    }
}

