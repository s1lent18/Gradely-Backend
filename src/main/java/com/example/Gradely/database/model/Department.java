package com.example.Gradely.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "departments")
public class Department {
    @Id
    private String id;

    private String departmentName;
    private String hod;

    public Department() {}

    public Department(String departmentName, String hod) {
        this.departmentName = departmentName;
        this.hod = hod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getHod() {
        return hod;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setHod(String hod) {
        this.hod = hod;
    }
}