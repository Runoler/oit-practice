package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_log")
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Column(name = "action_description")
    private String actionDescription;

    @Getter
    @Column(name = "action_date")
    private LocalDateTime actionDate;
}
