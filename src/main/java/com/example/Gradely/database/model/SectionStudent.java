package com.example.Gradely.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sectionstudents")
public class SectionStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sectionid", nullable = false)
    private Sections section;

    @ManyToOne
    @JoinColumn(name = "stdid", nullable = false)
    private Students student;

    public SectionStudent() {}

    public Students getStudent() {
        return student;
    }

    public Long getId() {
        return id;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSection(Sections section) {
        this.section = section;
    }

    public Sections getSection() {
        return section;
    }
}