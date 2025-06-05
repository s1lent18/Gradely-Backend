package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoursesRepository extends MongoRepository<Course, String> {

    Optional<Course> findByCourseCode(String courseCode);

    //List<Course> findAll();
}
