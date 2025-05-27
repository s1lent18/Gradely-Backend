package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.SectionStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionStudentRepository extends JpaRepository<SectionStudent, Long> {
}
