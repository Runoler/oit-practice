package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Tag {

    @Getter
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Getter
    @Column(name = "tag_name", nullable = false, unique = true)
    private String tagName;

    private Tag(UUID id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public static Tag createNewTag(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty.");
        }
        return new Tag(UUID.randomUUID(), tagName);
    }

    public void updateTagName(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty.");
        }
        this.tagName = tagName;
    }
}
