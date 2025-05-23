package com.example.Gradely.database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Courses {

    @Id
    @Column(name = "courseid")
    private String courseId;

    @Column(name = "coursename")
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "deptid", nullable = false)
    @JsonBackReference
    private Departments department;

    @Column(name = "status")
    private String status;

    @Column(name = "credithour")
    private Integer creditHour;

    @ManyToOne
    @JoinColumn(name = "prereqid")
    private Courses prereqCourse;

    @ManyToMany(mappedBy = "courses")
    private Set<Teachers> teachers = new HashSet<>();

    public Courses(String courseId, String courseName, Departments department, String status, Courses prereqCourse, Integer creditHour) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.status = status;
        this.prereqCourse = prereqCourse;
        this.creditHour = creditHour;
    }

    public Courses(String courseId, String courseName, Departments department, String status, Integer creditHour) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.status = status;
        this.creditHour = creditHour;
    }

    public Courses() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Courses getPrereqCourse() {
        return prereqCourse;
    }

    public String getCourseId() {
        return courseId;
    }


    public Set<Teachers> getTeachers() {
        return teachers;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public void setPrereqCourse(Courses prereqCourse) {
        this.prereqCourse = prereqCourse;
    }

    public void setTeachers(Set<Teachers> teachers) {
        this.teachers = teachers;
    }

    public Departments getDepartment() {
        return department;
    }

    public void setDepartment(Departments department) {
        this.department = department;
    }

    public Integer getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(Integer creditHour) {
        this.creditHour = creditHour;
    }
}
