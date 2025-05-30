package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachersRepository extends JpaRepository<Teachers, Long> {

    Optional<Teachers> findByAssignedEmail(String assignedEmail);
}
