package org.example.entities;

import java.util.Objects;
import java.util.UUID;

public class Tag {
    private UUID id;
    private String tagName;

    public Tag(UUID id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public Tag(String tagName) {
        this(UUID.randomUUID(), tagName);
    }

    public Tag() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }
    public String getTagName() { return tagName; }

    public void setId(UUID id) { this.id = id; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
