package com.example.Gradely.database.model;

import com.example.Gradely.service.AdminService;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "registrations")
public class Registrations {

    @Id
    private String id;
    private LocalDateTime createdAt;
    private List<AdminService.CourseRegistrationInit> availableCourses;

    public Registrations() {
        this.createdAt = LocalDateTime.now();
    }
}