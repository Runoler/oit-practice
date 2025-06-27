package org.example.repositories;

import org.example.entities.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserLogRepository extends JpaRepository<UserLog, UUID> {
}
