package com.example.Gradely.controller;

import com.example.Gradely.service.StudentService;
import com.example.Gradely.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public StudentController(
            StudentService studentService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil
    ) {
        this.studentService = studentService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public static class StudentRegisterCourses {
        public List<StudentService.StudentRegisterRequest> requests;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudentService.StudentResponse> addStudent(@RequestBody StudentService.StudentRequest request) {
        StudentService.StudentResponse response = studentService.add(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, StudentService.StudentGetResponse>> login(@RequestBody StudentService.StudentLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            StudentService.StudentGetResponse getResponse = studentService.getStudent(request.email);

            getResponse.token = jwtUtil.generateToken(userDetails);

            Map<String, StudentService.StudentGetResponse> response = new HashMap<>();
            response.put("studentData", getResponse);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            Map<String, StudentService.StudentGetResponse> response = new HashMap<>();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{studentId}/register")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<List<StudentService.CourseRegistration>> registerCourse(@PathVariable String studentId, @RequestBody StudentRegisterCourses request) {
        List<StudentService.CourseRegistration> response = studentService.registerCourses(studentId, request.requests);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}/getStudent")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<StudentService.StudentGetResponse> getStudent(@PathVariable String studentId) {
        StudentService.StudentGetResponse response = studentService.getStudent(studentId);
        return ResponseEntity.ok(response);
    }
}
