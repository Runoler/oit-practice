package org.example.repositories;

import org.example.entities.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCommentRepository extends JpaRepository<UserComment, UUID> {
}
