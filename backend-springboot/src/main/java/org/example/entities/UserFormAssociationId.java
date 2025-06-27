package org.example.entities;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public class UserFormAssociationId implements Serializable {

    private User user;
    private Form form;
}
