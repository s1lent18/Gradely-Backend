package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentlabs")
public class StudentLabs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdid", referencedColumnName = "stdid", nullable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid", referencedColumnName = "courseid", nullable = false)
    private Courses course;

    @Column(name = "labno", nullable = false)
    private int labNo;

    @Column(name = "marks")
    private String marks = "N/A";

    @Column(name = "total")
    private String total = "N/A";


    public void setTotal(String total) {
        this.total = total;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getTotal() {
        return total;
    }

    public String getMarks() {
        return marks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLabNo() {
        return labNo;
    }

    public void setLabNo(int labNo) {
        this.labNo = labNo;
    }
}