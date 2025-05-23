package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Courses;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, String> {

    @SuppressWarnings("null")
    @EntityGraph(attributePaths = "teachers")
    List<Courses> findAll();
}
