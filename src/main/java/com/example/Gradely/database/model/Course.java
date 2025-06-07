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
    private Integer creditHours;
    private String status;
    private String preReqCode;
    private List<TeacherInfo> teachers;
    private String departmentId;

    @Data
    public static class TeacherInfo {
        private String id;
        private String name;
        private String assignedEmail;
        private List<String> sections;

        public TeacherInfo() {}

        public TeacherInfo(String id, String name, String assignedEmail, List<String> sections) {
            this.id = id;
            this.name = name;
            this.assignedEmail = assignedEmail;
            this.sections = sections;
        }
    }

    public Course() {}

    public Course(String courseCode, String courseName, Integer creditHours, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.status = status;
    }

    public Course(String courseCode, String courseName, Integer creditHours, String status, String preReqCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.status = status;
        this.preReqCode = preReqCode;
    }

}