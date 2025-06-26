package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Sections;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends MongoRepository<Sections, String> {

    Optional<Sections> findByName(String name);
}
