package org.example.entities;

import java.util.Objects;
import java.util.UUID;

public class UserFormAssociation {
    private UUID userId;
    private UUID formId;
    private boolean isLeader;

    // Полный конструктор
    public UserFormAssociation(UUID userId, UUID formId, boolean isLeader) {
        this.userId = userId;
        this.formId = formId;
        this.isLeader = isLeader;
    }

    // Конструктор по умолчанию
    public UserFormAssociation() {}

    // Геттеры
    public UUID getUserId() { return userId; }
    public UUID getFormId() { return formId; }
    public boolean isLeader() { return isLeader; }

    // Сеттеры
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setFormId(UUID formId) { this.formId = formId; }
    public void setLeader(boolean leader) { isLeader = leader; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFormAssociation that = (UserFormAssociation) o;
        return Objects.equals(userId, that.userId) && Objects.equals(formId, that.formId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, formId);
    }
}
