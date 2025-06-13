package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Registrations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationsRepository extends MongoRepository<Registrations, String> {
    Optional<Registrations> findFirstByOrderByCreatedAtDesc();
}
