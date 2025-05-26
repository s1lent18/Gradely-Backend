package com.example.Gradely.controller;

import com.example.Gradely.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<String>> assignCourses(
        @PathVariable Long teacherId,
        @RequestBody CourseAssignmentRequest request
    ) {
        List<String> assignedCourses = teachersService.assignCoursesToTeacher(teacherId, request.courseIds);
        return ResponseEntity.ok(assignedCourses);
    }

    @DeleteMapping("/remove-courses-from-all")
    public ResponseEntity<Map<String, String>> removeCoursesFromAllTeachers() {
        teachersService.removeAllCoursesFromAllTeachers();

        Map<String, String> response = new HashMap<>();
        response.put("message", "All courses have been removed from all teachers.");

        return ResponseEntity.ok(response);
    }
}