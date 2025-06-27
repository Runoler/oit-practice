package org.example.repositories;

import org.example.entities.UserFormAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFormAssociationRepository extends JpaRepository<UserFormAssociation, UUID> {
}
