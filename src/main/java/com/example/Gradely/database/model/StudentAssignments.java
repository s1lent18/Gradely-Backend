package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentassignments")
public class StudentAssignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdid", referencedColumnName = "stdid", nullable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid", referencedColumnName = "courseid", nullable = false)
    private Courses course;


    @Column(name = "assignmentno", nullable = false)
    private int assignmentNo;

    @Column(name = "marks")
    private String marks = "N/A";

    @Column(name = "total")
    private String total = "N/A";

    public Long getId() {
        return id;
    }

    public Students getStudent() {
        return student;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public Courses getCourse() {
        return course;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTotal(String total) {
        this.total = total;
    }

    public int getAssignmentNo() {
        return assignmentNo;
    }

    public void setAssignmentNo(int assignmentNo) {
        this.assignmentNo = assignmentNo;
    }
}