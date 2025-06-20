package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends MongoRepository<Student, String> {

    Optional<Student> findByAssignedEmail(String assignedEmail);

    @Query("{ 'semesters.courses.courseId': ?0 }")
    List<Student> findByCourseId(String courseId);

}