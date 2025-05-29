package com.example.Gradely.controller;

import com.example.Gradely.service.AdminService;
import com.example.Gradely.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
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
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

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

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAllSections() {
        adminService.updateSections();
        return ResponseEntity.ok("Sections updated successfully.");
    }
}