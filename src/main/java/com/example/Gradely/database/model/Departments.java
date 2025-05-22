package com.example.Gradely.database.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
public class Departments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "did")
    private Long deptId;

    @Column(name = "dname")
    private String deptName;

    @Column(name = "hod")
    private String headOfDepartment;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Set<Courses> courses = new HashSet<>();

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public Set<Courses> getCourses() {
        return courses;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setCourses(Set<Courses> courses) {
        this.courses = courses;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public Departments() {}

    public Departments(Long deptId, String deptName, String headOfDepartment, Set<Courses> courses) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.headOfDepartment = headOfDepartment;
        this.courses = courses;
    }

    public Departments(Long deptId, String deptName, String headOfDepartment) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.headOfDepartment = headOfDepartment;
    }
}