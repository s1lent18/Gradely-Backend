package com.example.Gradely.controller;

import com.example.Gradely.database.model.Student;
import com.example.Gradely.service.AdminService;
import com.example.Gradely.service.StudentService;
import com.example.Gradely.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<StudentService.CourseRegistration>> registerCourse(@PathVariable String studentId, @RequestBody StudentService.StudentRegisterRequest request) {
        List<StudentService.CourseRegistration> response = studentService.registerCourses(studentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}/getStudent")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<StudentService.StudentGetResponse> getStudent(@PathVariable String studentId) {
        StudentService.StudentGetResponse response = studentService.getStudent(studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}/allowRegistration")
    public ResponseEntity<?> allowCourseRegistration(@PathVariable String studentId) {
        try {
            List<AdminService.CourseRegistrationInit> result = studentService.allowCourseRegistration(studentId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error during allowCourseRegistration: " + e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{studentId}/getSemesters")
    public ResponseEntity<?> getSemesters(@PathVariable String studentId) {
        Map<String, List<Student.Semester>> response = new HashMap<>();
        List<Student.Semester> list = studentService.getSemesters(studentId);
        response.put("semesters", list);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getAllResults")
    public ResponseEntity<?> getAllResults (@RequestBody StudentService.StudentAllResultRequest request) {
        Map<String, List<StudentService.Details>> response = new HashMap<>();
        List<StudentService.Details> list = studentService.getAllResults(request);
        response.put("allResults", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}/getAttendance")
    public ResponseEntity<?> getAttendance(@PathVariable String studentId) {

        Map<String, List<StudentService.StudentAttendanceRequest>> response = new HashMap<>();
        List<StudentService.StudentAttendanceRequest> list = studentService.getAttendance(studentId);
        response.put("attendance", list);
        return ResponseEntity.ok(response);
    }
}