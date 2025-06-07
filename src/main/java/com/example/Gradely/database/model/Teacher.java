package com.example.Gradely.database.model;

import java.security.SecureRandom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private String position;
    private List<String> qualification;
    private String status;
    private String gender;
    private String departmentId;
    private String dob;
    private String password;
    private List<Section> sections = new ArrayList<>();

    @Data
    public static class Section {
        private String name;
        private List<CourseInfo> course = new ArrayList<>();
    }

    @Data
    public static class CourseInfo {
        private String id;
        private int rating;
        private String worstRatedComment;
        private String bestRatedComment;

        public CourseInfo() {}

        public CourseInfo(String id, int rating, String worstRatedComment, String bestRatedComment) {
            this.id = id;
            this.rating = rating;
            this.worstRatedComment = worstRatedComment;
            this.bestRatedComment = bestRatedComment;
        }
    }

    public Teacher() {}

    public Teacher(String name, String personalEmail, String bloodGroup, String address, String phone, String emergency, String position, List<String> qualification, String gender, String dob) {
        this.name = name;
        this.personalEmail = personalEmail;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.phone = phone;
        this.emergency = emergency;
        this.position = position;
        this.hiringYear = String.valueOf(Year.now().getValue());
        this.qualification = qualification;
        this.gender = gender;
        this.dob = dob;
        this.password = generateRandomPassword();
        this.status = "Probation";
    }

    public List<CourseInfo> getCourses() {
        return sections.stream()
                .flatMap(section -> section.getCourse().stream())
                .collect(Collectors.toList());
    }
}