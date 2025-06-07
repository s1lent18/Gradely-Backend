package com.example.Gradely.service;

import com.example.Gradely.database.model.Course;
import com.example.Gradely.database.model.Student;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.StudentsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentsRepository studentsRepository;
    private final CoursesRepository courseRepository;

    public StudentService(StudentsRepository studentsRepository, CoursesRepository courseRepository) {
        this.studentsRepository = studentsRepository;
        this.courseRepository = courseRepository;
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

    public static class CourseRegistration {
        public String section;
        public String teacher;
        public String courseCode;
        public String courseName;
        public Integer creditHours;
        public String status;
        public PreReqResult preReqResult;
    }

    public static class PreReqResult {
        public String courseCode;
        public String courseName;
        public Integer creditHours;
        public String status;
        public Double gpa;
    }

    @Getter
    public static class StudentLoginRequest {
        public String email;
        public String password;

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
        public Integer semester;

        public StudentResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String phone, String emergency, String degree, String gender, String dob, String password, String assignedEmail, String batch, Integer semester) {
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
            this.semester = semester;
        }
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponse add(StudentRequest body) {

        Student student = new Student(body.studentName, body.fatherName, body.personalEmail, body.bloodGroup, body.address, body.phone, body.emergency, body.degree, body.gender, body.dob);

        Student savedStudent = studentsRepository.save(student);

        String assignedEmail = body.studentName.replaceAll("\\s+", ".").toLowerCase() + "@uni.com";

        savedStudent.setAssignedEmail(assignedEmail);

        String rawPassword = savedStudent.getPassword();

        savedStudent.setPassword(passwordEncoder.encode(savedStudent.getPassword()));

        studentsRepository.save(savedStudent);

        return new StudentResponse(
            String.valueOf(savedStudent.getId()),
            savedStudent.getName(),
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
            1
        );
    }

    @Transactional
    public List<CourseRegistration> registerCourses(String studentId, List<String> courseIds) {
        Student student = studentsRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student Not Found"));

        List<Course> courses = courseRepository.findAllById(courseIds);

        if (courses.size() != courseIds.size()) {
            throw new RuntimeException("Some course IDs are invalid");
        }

        List<CourseRegistration> registration = new ArrayList<>();

        for (Course course : courses) {
            CourseRegistration registrationForm = new CourseRegistration();
            registrationForm.courseCode = course.getCourseCode();
            registrationForm.courseName = course.getCourseName();
            registrationForm.creditHours = course.getCreditHours();
            registrationForm.status = course.getStatus();

            if (course.getPreReqCode() != null) {
                Course preReqCourse = courseRepository.findByCourseCode(course.getPreReqCode()).orElse(null);

                if (preReqCourse != null) {
                    PreReqResult preReq = new PreReqResult();
                    preReq.courseCode = preReqCourse.getCourseCode();
                    preReq.courseName = preReqCourse.getCourseName();
                    preReq.creditHours = preReqCourse.getCreditHours();

                    if (student.getSemesters() != null) {
                        for (Student.Semester semester : student.getSemesters()) {

                        }
                    }
                }

            }
        }

        return registration;
    }
}