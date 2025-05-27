package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.StudentQuizzes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuizzesRepository extends JpaRepository<StudentQuizzes, Long> {
}
