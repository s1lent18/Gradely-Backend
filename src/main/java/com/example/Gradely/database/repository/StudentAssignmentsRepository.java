package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.StudentAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAssignmentsRepository extends JpaRepository<StudentAssignments, Long> {
}
