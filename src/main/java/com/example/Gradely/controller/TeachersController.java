package com.example.Gradely.controller;

import com.example.Gradely.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeachersController {

    private final TeachersService teachersService;

    @Autowired
    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @PostMapping("/add")
    public ResponseEntity<TeachersService.TeachersResponse> addTeacher(@RequestBody TeachersService.TeacherRequest request) {
        TeachersService.TeachersResponse response = teachersService.add(request);
        return ResponseEntity.ok(response);
    }
}