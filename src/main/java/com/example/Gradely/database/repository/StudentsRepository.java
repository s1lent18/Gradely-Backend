package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentsRepository extends MongoRepository<Student, String> {

    Optional<Student> findByAssignedEmail(String assignedEmail);
}