package com.example.Gradely.controller;

import com.example.Gradely.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentsService) {
        this.studentService = studentsService;
    }

    @PostMapping("/add")
    public ResponseEntity<StudentService.StudentResponse> addStudent(@RequestBody StudentService.StudentRequest request) {
        StudentService.StudentResponse response = studentService.add(request);
        return ResponseEntity.ok(response);
    }
}
