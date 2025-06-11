package com.example.Gradely.database.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "sections")
public class Section {

    @Id
    private String id;

    private String name;
    private List<Class>  classes;

    @Data
    public static class Class {
        private String course;
        private String teacher;
        private List<StudentAttendance> studentAttendance;

        public Class() {}

        public Class(String course, String teacher, List<StudentAttendance> studentAttendance) {
            this.course = course;
            this.teacher = teacher;
            this.studentAttendance = studentAttendance;
        }
    }

    @Data
    public static class StudentAttendance {
        private String student;
        private List<AttendanceEntry> attendance;
    }

    @Data
    public static class AttendanceEntry {
        private String date;
        private List<Map<String, String>> status;
    }

    public Section() {}

    public Section(String name, List<Class> classes) {
        this.name = name;
        this.classes = classes;
    }
}