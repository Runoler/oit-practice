package org.example.repositories;

import org.example.entities.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, UUID> {
}
