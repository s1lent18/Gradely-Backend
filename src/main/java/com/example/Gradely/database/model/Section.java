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
    private Class  classes;

    @Data
    public static class Class {
        private String course;
        private String teacher;
        private List<String> students;
        private List<AttendanceEntry> attendance;

        public Class() {}

        public Class(String course, String teacher, List<String> students, List<AttendanceEntry> attendance) {
            this.course = course;
            this.teacher = teacher;
            this.students = students;
            this.attendance = attendance;
        }
    }

    @Data
    public static class AttendanceEntry {
        private String date;
        private List<Map<String, String>> status;
    }

    public Section() {}

    public Section(String name, Class classes) {
        this.name = name;
        this.classes = classes;
    }
}
