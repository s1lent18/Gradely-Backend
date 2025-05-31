package com.example.Gradely.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "departments")
public class Department {
    @Id
    private String id;

    private String departmentName;
    private String hod;
}
