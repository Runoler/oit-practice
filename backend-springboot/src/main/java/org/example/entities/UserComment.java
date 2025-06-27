package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_comment")
@AllArgsConstructor
@NoArgsConstructor
public class UserComment {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Getter
    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    @Getter
    @Setter
    @Column(name = "comment_text")
    private String commentText;

    @Getter
    @Column(name = "reputation")
    private int reputation;

    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserComment(User author, User target, String commentText, int reputation) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.target = target;
        this.commentText = commentText;
        setReputation(reputation);
        this.createdAt = LocalDateTime.now();
    }

    public void setReputation(int rep) {
        if (rep >= -1 && rep <= 1) {
            this.reputation += rep;
        } else {
            this.reputation = 0;
        }
    }
}
