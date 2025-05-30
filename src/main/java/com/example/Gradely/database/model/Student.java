package com.example.Gradely.database.model;

import jakarta.persistence.*;

import java.security.SecureRandom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collection = "students")
public class Student {

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#%&*!";

    private static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }

    @Id
    private String id;

    private String name;
    private String fatherName;
    private String personalEmail;
    private String assignedEmail;
    private String bloodGroup;
    private String address;
    private String phone;
    private String emergency;
    private String batch;
    private String degree;
    private String status;
    private String gender;
    private String dob;
    private double cgpa;
    private int chAttempted;
    private int chCleared;
    private String password;
    private Semester semesters;

    @Data
    public static class Semester {
        private int number;
        private List<Course> courses;
        private List<String> sections;
        private List<Double> gpa;
    }

    @Data
    public static class Course {
        private String courseCode;
        private String name;
        private List<List<String>> assignments;
        private List<List<String>> quizzes;
        private List<String> mid1;
        private List<String> mid2;
        private List<String> project;
        private List<String> classParticipation;
        private List<String> finalExam;
        private List<Attendance> attendance;
    }

    public Student() {}

    @Data
    public static class Attendance {
        private String date;
        private String status;
    }

    public static int getPasswordLength() {
        return PASSWORD_LENGTH;
    }

    public void setId(String id) {
        this.id = id;
    }
}