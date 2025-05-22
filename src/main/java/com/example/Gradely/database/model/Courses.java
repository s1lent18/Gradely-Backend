package com.example.Gradely.database.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseid")
    private Long courseId;

    @Column(name = "coursename")
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "deptid", nullable = false)
    private Departments department;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "prereqid")
    private Courses prereqCourse;

    @ManyToMany(mappedBy = "courses")
    private Set<Teachers> teachers = new HashSet<>();

    public Courses(Long courseId, String courseName, Departments department, String status, Courses prereqCourse) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.status = status;
        this.prereqCourse = prereqCourse;
    }

    public Courses(Long courseId, String courseName, Departments department, String status) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.status = status;
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

    public Long getCourseId() {
        return courseId;
    }


    public Set<Teachers> getTeachers() {
        return teachers;
    }

    public void setCourseId(Long courseId) {
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
}
