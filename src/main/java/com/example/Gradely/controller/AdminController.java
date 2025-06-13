package com.example.Gradely.controller;

import com.example.Gradely.service.AdminService;
import com.example.Gradely.service.StudentService;
import com.example.Gradely.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AdminService adminService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdminService.AdminLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getAdminEmail(), request.getAdminPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getAdminEmail());
            String jwt = jwtUtil.generateToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for email: {}", request.getAdminEmail(), e);
            Map<String, String> response = new HashMap<>();
            response.put("msg", "Invalid Email or Password " + request.adminEmail + " " + request.adminPassword);

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/setRegistration")
    public ResponseEntity<Map<String, String>> setCourseRegistration(@RequestBody List<AdminService.CourseRegistrationAdd> request) {
        String Id = adminService.addCoursesForRegistration(request);

        Map<String, String> response = new HashMap<>();
        response.put("Id:", Id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/allowRegistration")
    public ResponseEntity<List<AdminService.CourseRegistrationInit>> allowCourseRegistration() {
        List<AdminService.CourseRegistrationInit> result = adminService.allowCourseRegistration();
        return ResponseEntity.ok(result);
    }
}