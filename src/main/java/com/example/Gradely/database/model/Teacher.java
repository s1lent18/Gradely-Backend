package com.example.Gradely.database.model;

import java.security.SecureRandom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collection = "teachers")
public class Teacher {

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
    private String personalEmail;
    private String assignedEmail;
    private String bloodGroup;
    private String address;
    private String phone;
    private String emergency;
    private String hiringYear;
    private List<String> qualification;
    private String status;
    private String gender;
    private int departmentId;
    private int age;
    private String password;
    private List<CourseInfo> courses;

    @Data
    public static class CourseInfo {
        private int id;
        private int rating;
        private String worstRatedComment;
        private String bestRatedComment;
    }
}