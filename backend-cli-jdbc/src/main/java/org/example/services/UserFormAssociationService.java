package org.example.services;

import org.example.dao.FormDAO;
import org.example.dao.UserDAO;
import org.example.dao.UserFormAssociationDAO;
import org.example.entities.UserFormAssociation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFormAssociationService extends BaseService {
    private UserFormAssociationDAO userFormAssociationDAO;
    private UserService userService;
    private FormService formService;

    public UserFormAssociationService(Connection connection) {
        super(connection);
        this.userFormAssociationDAO = new UserFormAssociationDAO(getConnection());
        this.userService = new UserService(getConnection());
        this.formService = new FormService(getConnection());
    }

    public Optional<UserFormAssociation> createUserFormAssociation(UUID userId, UUID formId, boolean isLeader) {
        if (!userService.getUserById(userId).isPresent() || !formService.getFormById(formId).isPresent()) {
            System.out.println("Пользователь или форма не найдены для создания ассоциации.");
            return Optional.empty();
        }
        UserFormAssociation newAssociation = new UserFormAssociation(userId, formId, isLeader);
        return userFormAssociationDAO.create(newAssociation);
    }

    public Optional<UserFormAssociation> getUserFormAssociation(UUID userId, UUID formId) {
        return userFormAssociationDAO.findById(new UUID[]{userId, formId});
    }

    public List<UserFormAssociation> getAllUserFormAssociations() {
        return userFormAssociationDAO.findAll();
    }

    public Optional<UserFormAssociation> updateUserFormAssociation(UserFormAssociation association) {
        if (!userService.getUserById(association.getUserId()).isPresent() || !formService.getFormById(association.getFormId()).isPresent()) {
            System.out.println("Пользователь или форма не найдены для обновления ассоциации.");
            return Optional.empty();
        }
        return userFormAssociationDAO.update(association);
    }

    public boolean deleteUserFormAssociation(UUID userId, UUID formId) {
        return userFormAssociationDAO.delete(new UUID[]{userId, formId});
    }

    public List<UserFormAssociation> getAssociationsByUserId(UUID userId) {
        return userFormAssociationDAO.findAll().stream()
                .filter(a -> a.getUserId().equals(userId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<UserFormAssociation> getAssociationsByFormId(UUID formId) {
        return userFormAssociationDAO.findAll().stream()
                .filter(a -> a.getFormId().equals(formId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
