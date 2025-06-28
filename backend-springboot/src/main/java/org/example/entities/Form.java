package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "form")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Form {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Getter
    @Column(name = "title", nullable = false)
    private String title;

    @Getter
    @Column(name = "form_text", nullable = false)
    private String formText;

    @Getter
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Getter
    @Column(name = "is_complete", nullable = false)
    private boolean isComplete;

    @ManyToMany
    @JoinTable(
            name = "form_tags",
            joinColumns = @JoinColumn(name = "form_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private final Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<UserFormAssociation> userFormAssociations = new HashSet<>();

    private Form(UUID id, Activity activity, String title, String formText, LocalDateTime createdAt, boolean isComplete) {
        this.id = id;
        this.activity = activity;
        this.title = title;
        this.formText = formText;
        this.createdAt = createdAt;
        this.isComplete = isComplete;
    }

    public static Form createNewForm(Activity activity, String title, String formText) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Form title cannot be null or empty.");
        }
        return new Form(UUID.randomUUID(), activity, title, formText, LocalDateTime.now(), false);
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    public Set<UserFormAssociation> getUserFormAssociations() {
        return Collections.unmodifiableSet(this.userFormAssociations);
    }

    public void addTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null.");
        }
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public void addParticipant(UserFormAssociation userFormAssociation) {
        if (userFormAssociation == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (this.userFormAssociations.stream()
                .anyMatch(a -> a.getUser().equals(userFormAssociation.getUser()))) {
            throw new IllegalArgumentException("This user already in form.");
        }
        if (!userFormAssociation.getForm().equals(this)) {
            throw new IllegalArgumentException("This association has wrong form.");
        }
        this.userFormAssociations.add(userFormAssociation);
    }

    public void removeParticipant(UserFormAssociation userFormAssociation) {
        this.userFormAssociations.remove(userFormAssociation);
    }

    public void updateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    public void updateFormText(String formText) {
        if (formText == null || formText.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }
        this.formText = formText;
    }

    public void completeForm() {
        this.isComplete = true;
    }
}
