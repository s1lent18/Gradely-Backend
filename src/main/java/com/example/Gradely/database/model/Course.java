package com.example.Gradely.database.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collection = "courses")
public class Course {
    @Id
    private String id;

    private String courseCode;
    private String courseName;
    private Integer creditHours;
    private String status;
    private String preReqCode;
    private List<TeacherInfo> teachers;
    private String departmentId;

    @Data
    public static class TeacherInfo {
        private String id;
        private String name;
        private String assignedEmail;
        private List<String> sections;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getSections() {
            return sections;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setAssignedEmail(String assignedEmail) {
            this.assignedEmail = assignedEmail;
        }

        public String getAssignedEmail() {
            return assignedEmail;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSections(List<String> sections) {
            this.sections = sections;
        }

        public TeacherInfo() {}

        public TeacherInfo(String id, String name, String assignedEmail, List<String> sections) {
            this.id = id;
            this.name = name;
            this.assignedEmail = assignedEmail;
            this.sections = sections;
        }
    }

    public Course() {}

    public Course(String courseCode, String courseName, Integer creditHours, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.status = status;
    }

    public Course(String courseCode, String courseName, Integer creditHours, String status, String preReqCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.status = status;
        this.preReqCode = preReqCode;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public List<TeacherInfo> getTeachers() {
        return teachers;
    }

    public String getCourseName() {
        return courseName;
    }

    public Integer getCreditHours() {
        return creditHours;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeachers(List<TeacherInfo> teachers) {
        this.teachers = teachers;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCreditHours(Integer creditHours) {
        this.creditHours = creditHours;
    }

    public String getPreReqCode() {
        return preReqCode;
    }

    public void setPreReqCode(String preReqCode) {
        this.preReqCode = preReqCode;
    }
}