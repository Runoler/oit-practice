package org.example.repositories;

import org.example.entities.UserFormAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserFormAssociationRepository extends JpaRepository<UserFormAssociation, UUID> {
}
