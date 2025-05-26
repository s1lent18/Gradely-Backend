package com.example.Gradely.controller;

import com.example.Gradely.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeachersController {

    private final TeachersService teachersService;

    public static class CourseAssignmentRequest {
        public List<String> courseIds;
    }

    @Autowired
    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @PostMapping("/add")
    public ResponseEntity<TeachersService.TeachersResponse> addTeacher(@RequestBody TeachersService.TeacherRequest request) {
        TeachersService.TeachersResponse response = teachersService.add(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{teacherId}/assign-courses")
    public ResponseEntity<String> assignCourses(
            @PathVariable Long teacherId,
            @RequestBody CourseAssignmentRequest request
    ) {
        teachersService.assignCoursesToTeacher(teacherId, request.courseIds);
        return ResponseEntity.ok("Courses assigned successfully");
    }
}