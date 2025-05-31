package com.example.Gradely.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collection = "courses")
public class Course {
    @Id
    private String id;

    private String courseCode;
    private String courseName;
    private int creditHours;
    private String status;
    private int preReqId;
    private List<TeacherInfo> teachers;

    @Data
    public static class TeacherInfo {
        private int id;
        private String name;
        private List<String> sections;
    }
}