package com.example.Gradely.service;

import com.example.Gradely.database.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static class AdminLoginRequest {
        public String adminEmail;
        public String adminPassword;

        public String getAdminPassword() {
            return adminPassword;
        }

        public String getAdminEmail() {
            return adminEmail;
        }
    }


}
