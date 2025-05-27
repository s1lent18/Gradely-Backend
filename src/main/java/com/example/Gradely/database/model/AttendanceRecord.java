package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "attendancerecords")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sessionid", nullable = false)
    private AttendanceSession session;

    @ManyToOne
    @JoinColumn(name = "stdid", nullable = false)
    private Students student;

    @Column(name = "status", nullable = false)
    private String status;

    public AttendanceRecord() {}

    public Students getStudent() {
        return student;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public AttendanceSession getSession() {
        return session;
    }

    public Integer getId() {
        return id;
    }

    public void setSession(AttendanceSession session) {
        this.session = session;
    }
}