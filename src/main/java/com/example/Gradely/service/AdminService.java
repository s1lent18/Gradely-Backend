package com.example.Gradely.service;

import com.example.Gradely.database.model.Sections;
import com.example.Gradely.database.repository.AdminRepository;
import com.example.Gradely.database.repository.SectionsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final SectionsRepository sectionsRepository;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, SectionsRepository sectionsRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.sectionsRepository = sectionsRepository;
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

    @Transactional
    public void updateSections() {
        List<Sections> sections = sectionsRepository.findAll();

        for (Sections section : sections) {

            if (section.getSectionName().length() > 7) {
                char currentChar = section.getSectionName().charAt(7);

                if (Character.isDigit(currentChar)) {
                    int currentDigit = Character.getNumericValue(currentChar);

                    if (currentDigit < 8) {
                        int incrementedDigit = currentDigit + 1;
                        String updatedName = section.getSectionName().substring(0, 7) + incrementedDigit;
                        section.setSectionName(updatedName);
                    }
                }
            }

            if ("Fall".equalsIgnoreCase(section.getSemester())) {
                section.setSemester("Spring");
            } else if ("Spring".equalsIgnoreCase(section.getSemester())) {
                section.setSemester("Fall");
            }
        }

        sectionsRepository.saveAll(sections);
    }
}
