package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends MongoRepository<Section, String> {
}
