package org.example.repositories;

import org.example.entities.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, UUID> {
}
