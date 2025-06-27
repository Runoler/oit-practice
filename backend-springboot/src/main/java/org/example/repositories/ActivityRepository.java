package org.example.repositories;

import org.example.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
