package com.example.Gradely.database.model;

import jakarta.persistence.*;

import java.security.SecureRandom;

@Entity
@Table(name = "students")
public class Students {

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stdid")
    private Long studentId;

    @Column(name = "stdname")
    private String studentName;

    @Column(name = "fathername")
    private String fatherName;

    @Column(name = "bloodgroup")
    private String bloodGroup;

    @Column(name = "address")
    private String address;

    @Column(name = "assignedemail")
    private String assignedEmail;

    @Column(name = "personalemail")
    private String personalEmail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "emergency")
    private String emergency;

    @Column(name = "batch")
    private String batch;

    @Column(name = "degree")
    private String degree;

    @Column(name = "status")
    private String status;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
    private String dob;

    @Column(name = "cgpa")
    private Double cpga;

    @Column(name = "chattempted")
    private Integer chAttempted;

    @Column(name = "chcleared")
    private Integer chCleared;

    @Column(name = "password")
    private String password;

    public Students() {}

    public Students(String studentName, String fatherName, String bloodGroup, String address, String assignedEmail, String personalEmail, String phone, String emergency, String batch, String degree, String gender, String dob) {
        this.studentName = studentName;
        this.fatherName = fatherName;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.assignedEmail = assignedEmail;
        this.personalEmail = personalEmail;
        this.phone = phone;
        this.emergency = emergency;
        this.batch = batch;
        this.degree = degree;
        this.gender = gender;
        this.status = "Current";
        this.dob = dob;
        this.cpga = 0.0;
        this.chAttempted = 0;
        this.chCleared = 0;
        this.password = generateRandomPassword();
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getAddress() {
        return address;
    }

    public String getAssignedEmail() {
        return assignedEmail;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public String getEmergency() {
        return emergency;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAssignedEmail(String assignedEmail) {
        this.assignedEmail = assignedEmail;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getBatch() {
        return batch;
    }

    public String getDegree() {
        return degree;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getCpga() {
        return cpga;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Integer getChAttempted() {
        return chAttempted;
    }

    public Integer getChCleared() {
        return chCleared;
    }

    public String getStatus() {
        return status;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public void setChAttempted(Integer chAttempted) {
        this.chAttempted = chAttempted;
    }

    public void setChCleared(Integer chCleared) {
        this.chCleared = chCleared;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCpga(Double cpga) {
        this.cpga = cpga;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
