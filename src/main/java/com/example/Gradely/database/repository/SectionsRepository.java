package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionsRepository extends JpaRepository<Sections, Long> {
    boolean existsBySectionName(String sectionName);
}
