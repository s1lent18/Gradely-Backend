package com.example.Gradely.database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teachers {

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
    @Column(name = "teacherid")
    private Long teacherId;

    @Column(name = "teachername")
    private String teacherName;

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

    @Column(name = "gender")
    private String gender;

    @Column(name = "emergency")
    private String emergency;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "status")
    private String status;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "deptid", nullable = false)
    @JsonBackReference
    private Departments department;

    @ManyToMany
    @JoinTable(
        name = "courseteacher",
        joinColumns = @JoinColumn(name = "teacherid"),
        inverseJoinColumns = @JoinColumn(name = "courseid")
    )
    private Set<Courses> courses = new HashSet<>();

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmergency() {
        return emergency;
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

    public Set<Courses> getCourses() {
        return courses;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
    }

    public String getAddress() {
        return address;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getQualification() {
        return qualification;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setDepartment(Departments department) {
        this.department = department;
    }

    public Departments getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Teachers(String teacherName, String bloodGroup, String address, String assignedEmail, String personalEmail, String phone, String emergency, String qualification, String status, Set<Courses> courses, Departments department, String gender) {
        this.teacherName = teacherName;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.assignedEmail = assignedEmail;
        this.personalEmail = personalEmail;
        this.phone = phone;
        this.emergency = emergency;
        this.qualification = qualification;
        this.status = status;
        this.courses = courses;
        this.department = department;
        this.gender = gender;
        this.password = generateRandomPassword();
    }

    public Teachers(String teacherName, String bloodGroup, String address, String assignedEmail, String personalEmail, String phone, String emergency, String qualification, String status, Departments department, String gender) {
        this.teacherName = teacherName;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.assignedEmail = assignedEmail;
        this.personalEmail = personalEmail;
        this.phone = phone;
        this.emergency = emergency;
        this.qualification = qualification;
        this.status = status;
        this.department = department;
        this.gender = gender;
        this.password = generateRandomPassword();
    }
}