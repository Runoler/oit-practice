package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "form")
@AllArgsConstructor
@NoArgsConstructor
public class Form {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Getter
    @Setter
    @Column(name = "title")
    private String title;

    @Getter
    @Setter
    @Column(name = "form_text")
    private String formText;

    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Column(name = "is_complete")
    private boolean isComplete;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<UserFormAssociation> userFormAssociations = new HashSet<>();

    public Form(Activity activity, String title, String formText) {
        this.id = UUID.randomUUID();
        this.activity = activity;
        this.title = title;
        this.formText = formText;
        this.createdAt = LocalDateTime.now();
        this.isComplete = false;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    public Set<UserFormAssociation> getUserFormAssociations() {
        return Collections.unmodifiableSet(this.userFormAssociations);
    }

    public void addTag(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("tag cannot be null.");
        }
        if (tag.getTagName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name must be non-empty.");
        }
        this.tags.add(tag);
        tag.addForm(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.removeForm(this);
    }

    public void addUserFormAssociation(UserFormAssociation userFormAssociation) {
        if (userFormAssociation == null) {
            throw new IllegalArgumentException("Association cannot be null.");
        }
        if (!userFormAssociation.getForm().equals(this)) {
            throw new IllegalArgumentException("Form must contain only associated records.");
        }
        this.userFormAssociations.add(userFormAssociation);
        userFormAssociation.setForm(this);
    }

    public void removeUserFormAssociation(UserFormAssociation userFormAssociation) {
        if (userFormAssociation == null) {
            throw new IllegalArgumentException("Association cannot be null.");
        }
        if (!this.userFormAssociations.contains(userFormAssociation)) {
            return;
        }
        this.userFormAssociations.remove(userFormAssociation);
        userFormAssociation.setForm(null);
    }
}
