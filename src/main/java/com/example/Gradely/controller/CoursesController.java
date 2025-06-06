package com.example.Gradely.controller;

import com.example.Gradely.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesService coursesService;

    public static class TeacherAssignmentRequest {
        public List<String> teacherIds;
        public List<String> sections;
    }

    @Autowired
    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CoursesService.CourseResponse> addCourse(@RequestBody CoursesService.CourseRequest request) {
        CoursesService.CourseResponse response = coursesService.add(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/assign-teachers")
    public ResponseEntity<String> assignTeachersToCourse(
            @PathVariable String courseId,
            @RequestBody TeacherAssignmentRequest request
    ) {
        coursesService.assignTeachersToCourse(courseId, request.teacherIds, request.sections);
        return ResponseEntity.ok("Teachers assigned to course successfully");
    }

    @GetMapping("/getWithTeachers")
    public ResponseEntity<List<CoursesService.CourseWithTeachersResponse>> getCoursesWithTeachers() {
        List<CoursesService.CourseWithTeachersResponse> response = coursesService.getAllCoursesWithTeachers();
        return ResponseEntity.ok(response);
    }
}
