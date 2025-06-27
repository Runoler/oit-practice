package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(UserFormAssociationId.class)
@Table(name = "user_form_association")
@AllArgsConstructor
@NoArgsConstructor
public class UserFormAssociation {

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @Id
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @Getter
    @Setter
    @Column(name = "is_leader")
    private boolean isLeader;
}
