package com.example.Gradely.service;

import com.example.Gradely.database.model.Teacher;
import com.example.Gradely.database.repository.TeachersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.Gradely.database.model.Admin;
import com.example.Gradely.database.model.Student;
import com.example.Gradely.database.repository.AdminRepository;
import com.example.Gradely.database.repository.StudentsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private StudentsRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Student> student = studentRepository.findByAssignedEmail(email);
        if (student.isPresent()) {
            return createUserDetails(student.get(), "STUDENT");
        }

        Optional<Admin> admin = adminRepository.findByAdminEmail(email);
        if (admin.isPresent()) {
            return createUserDetails(admin.get(), "ADMIN");
        }

        Optional<Teacher> teacher = teachersRepository.findByAssignedEmail(email);
        if(teacher.isPresent()) {
            return createUserDetails(teacher.get(), "TEACHER");
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private UserDetails createUserDetails(Object user, String role) {
        if (user instanceof Student student) {
            return new org.springframework.security.core.userdetails.User(
                student.getAssignedEmail(),
                student.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
            );
        } else if (user instanceof Admin admin) {
            return new org.springframework.security.core.userdetails.User(
                admin.getAdminEmail(),
                admin.getAdminPassword(),
                List.of(new SimpleGrantedAuthority(role))
            );
        } else if (user instanceof  Teacher teacher) {
            return new org.springframework.security.core.userdetails.User(
                teacher.getAssignedEmail(),
                teacher.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
            );
        }
        throw new IllegalArgumentException("Unknown user type");
    }
}
