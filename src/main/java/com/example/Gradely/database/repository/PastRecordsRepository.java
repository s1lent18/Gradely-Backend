package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.PastRecords;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastRecordsRepository extends MongoRepository<PastRecords, String> {
}