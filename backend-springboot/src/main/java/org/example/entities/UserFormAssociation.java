package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(UserFormAssociationId.class)
@Table(name = "user_form_association")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class UserFormAssociation {

    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false, updatable = false)
    private Form form;

    @Getter
    @Column(name = "is_leader", nullable = false)
    private boolean isLeader;

    private UserFormAssociation(User user, Form form, boolean isLeader) {
        this.user = user;
        this.form = form;
        this.isLeader = isLeader;
    }

    public static UserFormAssociation createNewUserFormAssociation(User user, Form form, boolean isLeader) {
        if (user == null || form == null) {
            throw new IllegalArgumentException("User or form cannot be null.");
        }
        return new UserFormAssociation(user, form, isLeader);
    }

    public void makeLeader() {
        this.isLeader = true;
    }

    public void removeLeader() {
        this.isLeader = false;
    }
}
