package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {

    Optional<Admin> findByAdminEmail(String adminEmail);
}
