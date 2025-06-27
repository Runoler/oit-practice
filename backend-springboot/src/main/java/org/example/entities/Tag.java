package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tag")
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable {

    @Getter
    @Id
    @Column(name = "id")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "tag_name")
    private String tagName;

    @ManyToMany
    private Set<Form> forms = new HashSet<>();

    public Tag(String tagName) {
        this.id = UUID.randomUUID();
        this.tagName = tagName;
    }

    public Set<Form> getForms() {
        return Collections.unmodifiableSet(forms);
    }

    public void addForm(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Form cannot be null");
        }
        if (!form.getTags().contains(this)) {
            throw new IllegalArgumentException("Form must contain this tag.");
        }
        this.forms.add(form);
    }

    public void removeForm(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Form cannot be null");
        }
        this.forms.remove(form);
    }

}
