package com.example.Gradely.service;

import com.example.Gradely.database.model.Students;
import com.example.Gradely.database.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StudentService {

    private final StudentsRepository studentsRepository;

    public StudentService(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    public static class StudentRequest {
        public String studentName;
        public String fatherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String degree;
        public String gender;
        public String dob;
    }

    public static class StudentLoginRequest {
        public String email;
        public String password;

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class StudentResponse {
        public String studentId;
        public String studentName;
        public String fatherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String batch;
        public String degree;
        public String gender;
        public String dob;
        public String password;
        public String assignedEmail;
        public String section;

        public StudentResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String phone, String emergency, String degree, String gender, String dob, String password, String assignedEmail, String batch, String section) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.fatherName = fatherName;
            this.bloodGroup = bloodGroup;
            this.address = address;
            this.personalEmail = personalEmail;
            this.phone = phone;
            this.emergency = emergency;
            this.batch = batch;
            this.degree = degree;
            this.gender = gender;
            this.dob = dob;
            this.password = password;
            this.assignedEmail = assignedEmail;
            this.section = section;
        }
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponse add(StudentRequest body) {
        String batch = String.valueOf(LocalDate.now().getYear());

        String section = assignSection(body.degree, batch);

        Students student = new Students(body.studentName, body.fatherName, body.bloodGroup, body.address, "", body.personalEmail, body.phone, body.emergency, batch, body.degree, body.gender, body.dob, section);

        Students savedStudent = studentsRepository.save(student);

        String assignedEmail = body.studentName.replaceAll("\\s+", ".").toLowerCase() + "." + savedStudent.getStudentId() + "@uni.com";

        savedStudent.setAssignedEmail(assignedEmail);

        String rawPassword = savedStudent.getPassword();

        savedStudent.setPassword(passwordEncoder.encode(savedStudent.getPassword()));

        studentsRepository.save(savedStudent);

        return new StudentResponse(
            String.valueOf(savedStudent.getStudentId()),
            savedStudent.getStudentName(),
            savedStudent.getFatherName(),
            savedStudent.getBloodGroup(),
            savedStudent.getAddress(),
            savedStudent.getPersonalEmail(),
            savedStudent.getPhone(),
            savedStudent.getEmergency(),
            savedStudent.getDegree(),
            savedStudent.getGender(),
            savedStudent.getDob(),
            rawPassword,
            savedStudent.getAssignedEmail(),
            savedStudent.getBatch(),
            savedStudent.getSection()
        );
    }

    private String assignSection(String degree, String batch) {
        List<Character> sectionSuffixes = new ArrayList<>();
        for (char c = 'A'; c <= 'J'; c++) {
            sectionSuffixes.add(c);
        }

        Collections.shuffle(sectionSuffixes);

        for (char suffix : sectionSuffixes) {
            String section = degree + "-1" + suffix;
            long count = studentsRepository.countByDegreeAndBatchAndSection(degree, batch, section);
            if (count < 50) {
                return section;
            }
        }

        throw new RuntimeException("All sections for degree " + degree + " in batch " + batch + " are full.");
    }
}