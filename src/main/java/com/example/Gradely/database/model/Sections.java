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

    @Column(name = "semester", nullable = false)
    private String semester;

    public Sections() {}

    public Sections(String sectionName, String semester) {
        this.sectionName = sectionName;
        this.semester = semester;
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

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}