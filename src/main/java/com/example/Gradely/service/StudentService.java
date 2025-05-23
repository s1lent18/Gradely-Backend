package com.example.Gradely.service;

import com.example.Gradely.database.model.Students;
import com.example.Gradely.database.repository.StudentsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

        public StudentResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String phone, String emergency, String degree, String gender, String dob, String password, String assignedEmail, String batch) {
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
        }
    }

    @Transactional
    public StudentResponse add(StudentRequest body) {
        Students student = new Students(body.studentName, body.fatherName, body.bloodGroup, body.address, "", body.personalEmail, body.phone, body.emergency, String.valueOf(java.time.LocalDate.now().getYear()), body.degree, body.gender, body.dob);

        Students savedStudent = studentsRepository.save(student);

        String assignedEmail = body.studentName.replaceAll("\\s+", ".").toLowerCase() + "." + savedStudent.getStudentId() + "@uni.com";

        savedStudent.setAssignedEmail(assignedEmail);

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
                savedStudent.getPassword(),
                savedStudent.getAssignedEmail(),
                savedStudent.getBatch()
        );
    }
}
