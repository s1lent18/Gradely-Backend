package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {
}
