package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentsRepository extends MongoRepository<Department, String> { }
