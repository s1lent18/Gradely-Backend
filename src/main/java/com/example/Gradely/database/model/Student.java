package com.example.Gradely.database.model;


import java.io.Serializable;
import java.security.SecureRandom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.Year;
import java.util.ArrayList;
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
    private int warningCount;
    private String password;
    private List<Semester> semesters;

    @Data
    public static class Semester {
        private int number;
        private String name;
        private Integer creditsRegistered;
        private Integer creditsEarned;
        private List<Courses> courses;
    }

    @Data
    public static class Courses {
        private String courseId;
        private String section;
        private Double gpa;
        private String grade;
        private Course details;
        private List<Attendance> attendance;
        private List<String> savePoints;
    }

    @Data
    public static class Course {
        private String courseCode;
        private String name;
        private Integer creditCount;
        private List<Assignment> assignments;
        private List<Quiz> quizzes;
        private Exam mid1;
        private Exam mid2;
        private String projectScore;
        private String projectTotal;
        private String classParticipationScore;
        private String classParticipationTotal;
        private Exam finalExam;

        public Course(String courseCode, String name, Integer creditCount) {
            this.courseCode = courseCode;
            this.name = name;
            this.creditCount = creditCount;
            this.assignments = new ArrayList<>();
            this.quizzes = new ArrayList<>();
            this.mid1 = new Exam();
            this.mid2 = new Exam();
            this.classParticipationScore = "?";
            this.classParticipationTotal = "?";
            this.projectScore = "?";
            this.projectTotal = "?";
            this.finalExam = new Exam();
        }
    }

    @Data
    public static class Exam {
        private String examScore;
        private String examTotal;
        private String weightage;
        private List<QuestionBreakDown> breakDowns;

        public Exam() {
            this.examScore = "?";
            this.examTotal = "?";
            this.weightage = "?";
            this.breakDowns = new ArrayList<>();
        }
    }

    @Data
    public static class QuestionBreakDown {
        public String questionScore;
        public String questionTotal;
        public String questionWeightage;

        public QuestionBreakDown() {
            this.questionScore = "?";
            this.questionTotal = "?";
            this.questionWeightage = "?";
        }
    }

    @Data
    public static class Quiz {
        private String weightage;
        private String quizScore;
        private String quizTotal;

        public Quiz() {
            this.weightage = "?";
            this.quizScore = "?";
            this.quizTotal = "?";
        }
    }

    @Data
    public static class Assignment {
        private String weightage;
        private String assignmentScore;
        private String assignmentTotal;

        public Assignment() {
            this.weightage = "?";
            this.assignmentScore = "?";
            this.assignmentTotal = "?";
        }
    }

    public Student() {}

    public Student(
            String name,
            String fatherName,
            String personalEmail,
            String bloodGroup,
            String address,
            String phone,
            String emergency,
            String degree,
            String gender,
            String dob
    ) {
        this.name = name;
        this.fatherName = fatherName;
        this.personalEmail = personalEmail;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.phone = phone;
        this.emergency = emergency;
        this.batch = String.valueOf(Year.now().getValue());
        this.degree = degree;
        this.status = "Current";
        this.gender = gender;
        this.dob = dob;
        this.cgpa = 0.0;
        this.chAttempted = 0;
        this.chCleared = 0;
        this.warningCount = 0;
        this.password = generateRandomPassword();
    }

    @Data
    public static class Attendance implements Serializable {
        private String date;
        private String status;
    }
}