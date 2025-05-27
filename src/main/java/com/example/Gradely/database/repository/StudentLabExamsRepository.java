package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.StudentLabExams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentLabExamsRepository extends JpaRepository<StudentLabExams, Long> {
}
