package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentlabexams")
public class StudentLabExams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdid", referencedColumnName = "stdid", nullable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid", referencedColumnName = "courseid", nullable = false)
    private Courses course;

    @Column(name = "midmarks")
    private String midMarks = "N/A";

    @Column(name = "midtotal")
    private String midTotal = "N/A";

    @Column(name = "finalmarks")
    private String finalMarks = "N/A";

    @Column(name = "finaltotal")
    private String finalTotal = "N/A";

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Students getStudent() {
        return student;
    }

    public Courses getCourse() {
        return course;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public String getFinalMarks() {
        return finalMarks;
    }

    public String getFinalTotal() {
        return finalTotal;
    }

    public String getMidMarks() {
        return midMarks;
    }

    public String getMidTotal() {
        return midTotal;
    }

    public void setFinalMarks(String finalMarks) {
        this.finalMarks = finalMarks;
    }

    public void setFinalTotal(String finalTotal) {
        this.finalTotal = finalTotal;
    }

    public void setMidMarks(String midMarks) {
        this.midMarks = midMarks;
    }

    public void setMidTotal(String midTotal) {
        this.midTotal = midTotal;
    }
}