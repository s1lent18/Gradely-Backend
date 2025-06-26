package com.example.Gradely.controller;

import com.example.Gradely.database.model.Sections;
import com.example.Gradely.service.TeachersService;
import com.example.Gradely.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeachersController {

    private final TeachersService teachersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public static class CourseAssignmentRequest {
        public String courseId;
        public String sectionId;
    }

    @Autowired
    public TeachersController(TeachersService teachersService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.teachersService = teachersService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TeachersService.TeachersResponse> addTeacher(@RequestBody TeachersService.TeacherRequest request) {
        TeachersService.TeachersResponse response = teachersService.add(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, TeachersService.TeachersGetResponse>> login(@RequestBody TeachersService.TeacherLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            TeachersService.TeachersGetResponse getResponse = teachersService.getTeacher(request.email);
            getResponse.token = jwtUtil.generateToken(userDetails);

            Map<String, TeachersService.TeachersGetResponse> response = new HashMap<>();
            response.put("teacherData", getResponse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, TeachersService.TeachersGetResponse> response = new HashMap<>();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{teacherId}/assign-courses")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Sections> assignCourses(
        @PathVariable String teacherId,
        @RequestBody CourseAssignmentRequest request
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User Authorities: " + auth.getAuthorities());

        return ResponseEntity.ok(teachersService.assignCourseToTeacher(request.sectionId, teacherId, request.courseId));
    }

    @DeleteMapping("/remove-courses-from-all")
    public ResponseEntity<Map<String, String>> removeCoursesFromAllTeachers() {
        teachersService.removeAllCoursesFromAllTeachers();

        Map<String, String> response = new HashMap<>();
        response.put("message", "All courses have been removed from all teachers.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{teacherId}/markStudents")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<Map<String, TeachersService.TeacherMarkingRequest>> markStudents(
            @PathVariable String teacherId,
            @RequestBody TeachersService.TeacherMarkingRequest request
    ) {
        TeachersService.TeacherMarkingRequest requests = teachersService.markStudents(teacherId, request);

        Map<String, TeachersService.TeacherMarkingRequest> response = new HashMap<>();
        response.put("markings", requests);

        return ResponseEntity.ok(response);
    }
}