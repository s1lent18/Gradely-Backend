package com.example.Gradely.database.model;


import java.security.SecureRandom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.Year;
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
        this.password = generateRandomPassword();
    }

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

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmergency() {
        return emergency;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public static String getCharSet() {
        return CHAR_SET;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getAssignedEmail() {
        return assignedEmail;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getDegree() {
        return degree;
    }

    public String getBatch() {
        return batch;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public double getCgpa() {
        return cgpa;
    }

    public int getChAttempted() {
        return chAttempted;
    }

    public int getChCleared() {
        return chCleared;
    }

    public Semester getSemesters() {
        return semesters;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setAssignedEmail(String assignedEmail) {
        this.assignedEmail = assignedEmail;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setChCleared(int chCleared) {
        this.chCleared = chCleared;
    }

    public void setChAttempted(int chAttempted) {
        this.chAttempted = chAttempted;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public void setSemesters(Semester semesters) {
        this.semesters = semesters;
    }
}