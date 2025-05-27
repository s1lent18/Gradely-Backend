package com.example.Gradely.database.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "attendancesessions")
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sectionid", nullable = false)
    private Sections section;

    @Column(name = "sessiondate", nullable = false)
    private LocalDate sessionDate;

    public AttendanceSession() {}

    public Sections getSection() {
        return section;
    }

    public void setSection(Sections section) {
        this.section = section;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }
}