package org.example.services;

import org.example.dao.UserCommentDAO;
import org.example.dao.UserDAO;
import org.example.entities.UserComment;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserCommentService extends BaseService {
    private UserCommentDAO userCommentDAO;
    private UserService userService;

    public UserCommentService(Connection connection) {
        super(connection);
        this.userCommentDAO = new UserCommentDAO(getConnection());
        this.userService = new UserService(getConnection());
    }

    public Optional<UserComment> addComment(UUID authorId, UUID targetId, String commentText, int reputation) {
        if (!userService.getUserById(authorId).isPresent() || !userService.getUserById(targetId).isPresent()) {
            System.out.println("Автор или целевой пользователь не найдены для добавления комментария.");
            return Optional.empty();
        }
        UserComment newComment = new UserComment(authorId, targetId, commentText, reputation);
        return userCommentDAO.create(newComment);
    }

    public Optional<UserComment> getCommentById(UUID commentId) {
        return userCommentDAO.findById(commentId);
    }

    public List<UserComment> getAllComments() {
        return userCommentDAO.findAll();
    }

    public Optional<UserComment> updateComment(UserComment comment) {
        if (!userService.getUserById(comment.getAuthorId()).isPresent() || !userService.getUserById(comment.getTargetId()).isPresent()) {
            System.out.println("Автор или целевой пользователь не найдены для обновления комментария.");
            return Optional.empty();
        }
        return userCommentDAO.update(comment);
    }

    public boolean deleteComment(UUID commentId) {
        return userCommentDAO.delete(commentId);
    }

    public List<UserComment> getCommentsByAuthor(UUID authorId) {
        return userCommentDAO.findAll().stream()
                .filter(c -> c.getAuthorId().equals(authorId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<UserComment> getCommentsAboutUser(UUID targetId) {
        return userCommentDAO.findAll().stream()
                .filter(c -> c.getTargetId().equals(targetId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
