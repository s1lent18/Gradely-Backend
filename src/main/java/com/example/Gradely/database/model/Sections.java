package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sections")
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sectionname", nullable = false)
    private String sectionName;

    @ManyToOne
    @JoinColumn(name = "courseid", nullable = false)
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "teacherid", nullable = false)
    private Teachers teacher;

    @Column(name = "semester", nullable = false)
    private String semester;

    public Sections() {}

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getSemester() {
        return semester;
    }

    public Teachers getTeacher() {
        return teacher;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setTeacher(Teachers teacher) {
        this.teacher = teacher;
    }
}