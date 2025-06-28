package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class UserComment {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private User author;

    @Getter
    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false, updatable = false)
    private User target;

    @Getter
    @Setter
    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Getter
    @Column(name = "reputation", nullable = false, updatable = false)
    private int reputation;

    @Getter
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private UserComment(UUID id, User author, User target, String commentText, int reputation, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.target = target;
        this.commentText = commentText;
        this.reputation = reputation;
        this.createdAt = createdAt;
    }

    public static UserComment createNewUserComment(User author, User target, String commentText, int reputation) {
        if (author == null || target == null) {
            throw new IllegalArgumentException("Author or target cannot be null.");
        }
        if (commentText == null) {
            throw new IllegalArgumentException("Comment text cannot be null.");
        }
        if (reputation < -1 || reputation > 1) {
            throw new IllegalArgumentException("Reputation has wrong value.");
        }
        return new UserComment(UUID.randomUUID(), author, target, commentText, reputation, LocalDateTime.now());
    }

    public void updateCommentText(String commentText) {
        if (commentText == null) {
            throw new IllegalArgumentException("Comment text cannot be null.");
        }
        this.commentText = commentText;
    }
}
