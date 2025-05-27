package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentmarks")
public class StudentMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdid", referencedColumnName = "stdid", nullable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid", referencedColumnName = "courseid", nullable = false)
    private Courses course;

    @Column(name = "mid1marks")
    private String mid1Marks = "N/A";

    @Column(name = "mid1total")
    private String mid1Total = "N/A";

    @Column(name = "mid2marks")
    private String mid2Marks = "N/A";

    @Column(name = "mid2total")
    private String mid2Total = "N/A";

    @Column(name = "projectmarks")
    private String projectMarks = "N/A";

    @Column(name = "projecttotal")
    private String projectTotal = "N/A";

    @Column(name = "classparticipationmarks")
    private String classParticipationMarks = "N/A";

    @Column(name = "classparticipationtotal")
    private String classParticipationTotal = "N/A";

    @Column(name = "miscmarks")
    private String miscMarks = "N/A";

    @Column(name = "misctotal")
    private String miscTotal = "N/A";

    public StudentMarks() {}

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public String getMid1Marks() {
        return mid1Marks;
    }

    public String getClassParticipationMarks() {
        return classParticipationMarks;
    }

    public String getMid1Total() {
        return mid1Total;
    }

    public String getMid2Marks() {
        return mid2Marks;
    }

    public String getMid2Total() {
        return mid2Total;
    }

    public void setMid1Marks(String mid1Marks) {
        this.mid1Marks = mid1Marks;
    }

    public String getMiscMarks() {
        return miscMarks;
    }

    public String getProjectMarks() {
        return projectMarks;
    }

    public String getProjectTotal() {
        return projectTotal;
    }

    public String getClassParticipationTotal() {
        return classParticipationTotal;
    }

    public void setMid1Total(String mid1Total) {
        this.mid1Total = mid1Total;
    }

    public void setClassParticipationMarks(String classParticipationMarks) {
        this.classParticipationMarks = classParticipationMarks;
    }

    public void setMid2Marks(String mid2Marks) {
        this.mid2Marks = mid2Marks;
    }

    public String getMiscTotal() {
        return miscTotal;
    }

    public void setClassParticipationTotal(String classParticipationTotal) {
        this.classParticipationTotal = classParticipationTotal;
    }

    public void setMid2Total(String mid2Total) {
        this.mid2Total = mid2Total;
    }

    public void setProjectMarks(String projectMarks) {
        this.projectMarks = projectMarks;
    }

    public void setMiscMarks(String miscMarks) {
        this.miscMarks = miscMarks;
    }

    public void setProjectTotal(String projectTotal) {
        this.projectTotal = projectTotal;
    }

    public void setMiscTotal(String miscTotal) {
        this.miscTotal = miscTotal;
    }
}