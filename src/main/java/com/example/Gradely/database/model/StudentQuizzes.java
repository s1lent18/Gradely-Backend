package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentquizzes")
public class StudentQuizzes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdid", referencedColumnName = "stdid")
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid", referencedColumnName = "courseid", nullable = false)
    private Courses course;

    @Column(name = "quizno", nullable = false)
    private int quizNo;

    @Column(name = "marks")
    private String marks = "N/A";

    @Column(name = "total")
    private String total = "N/A";

    public StudentQuizzes() {}

    public void setCourse(Courses course) {
        this.course = course;
    }

    public Courses getCourse() {
        return course;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public int getQuizNo() {
        return quizNo;
    }

    public String getMarks() {
        return marks;
    }

    public String getTotal() {
        return total;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public void setQuizNo(int quizNo) {
        this.quizNo = quizNo;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}