package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachersRepository extends MongoRepository<Teacher, String> {

    Optional<Teacher> findByAssignedEmail(String assignedEmail);
}
