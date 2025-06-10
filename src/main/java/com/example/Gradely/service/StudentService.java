package com.example.Gradely.service;

import com.example.Gradely.database.model.Course;
import com.example.Gradely.database.model.Section;
import com.example.Gradely.database.model.Student;
import com.example.Gradely.database.model.Teacher;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.SectionRepository;
import com.example.Gradely.database.repository.StudentsRepository;
import com.example.Gradely.database.repository.TeachersRepository;
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
    private final SectionRepository sectionRepository;
    private final TeachersRepository teachersRepository;

    public StudentService(StudentsRepository studentsRepository, CoursesRepository courseRepository, SectionRepository sectionRepository, TeachersRepository teachersRepository) {
        this.studentsRepository = studentsRepository;
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.teachersRepository = teachersRepository;
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

    public static class StudentGetResponse {
        public String token;
        public String studentId;
        public String studentName;
        public String fatherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String assignedEmail;
        public String phone;
        public String emergency;
        public String batch;
        public String degree;
        public String gender;
        public String dob;
        public List<Student.Semester> semesters;

        public StudentGetResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String assignedEmail, String phone, String emergency, String batch, String degree, String gender, String dob, List<Student.Semester> semesters) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.fatherName = fatherName;
            this.bloodGroup = bloodGroup;
            this.address = address;
            this.personalEmail = personalEmail;
            this.assignedEmail = assignedEmail;
            this.phone = phone;
            this.emergency = emergency;
            this.batch = batch;
            this.degree = degree;
            this.gender = gender;
            this.dob = dob;
            this.semesters = semesters;
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
    public List<CourseRegistration> registerCourses(String studentId, List<String> courseIds, List<String> sectionIds, List<String> teacherIds) {
        Student student = studentsRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student Not Found"));

        List<Course> courses = courseRepository.findAllById(courseIds);

        List<Section> sections = sectionRepository.findAllById(sectionIds);

        List<Teacher> teachers = teachersRepository.findAllById(teacherIds);

        if (courses.size() != courseIds.size()) {
            throw new RuntimeException("Some course IDs are invalid");
        }

        if (sections.size() != courseIds.size() || teachers.size() != courseIds.size()) {
            throw new RuntimeException("Section or Teacher mapping mismatch with courses");
        }

        int currentSemesterNumber = (student.getSemesters() != null && !student.getSemesters().isEmpty())
                ? student.getSemesters().get(student.getSemesters().size() - 1).getNumber() + 1
                : 1;

        Student.Semester semester = new Student.Semester();
        semester.setNumber(currentSemesterNumber);
        semester.setCourses(new ArrayList<>());
        semester.setSections(new ArrayList<>());
        semester.setGpa(new ArrayList<>());

        List<CourseRegistration> registration = new ArrayList<>();

        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            Section section = sections.get(i);
            Teacher teacher = teachers.get(i);

            CourseRegistration registrationForm = new CourseRegistration();
            registrationForm.courseCode = course.getCourseCode();
            registrationForm.courseName = course.getCourseName();
            registrationForm.creditHours = course.getCreditHours();
            registrationForm.status = course.getStatus();
            registrationForm.section = section.getId();
            registrationForm.teacher = teacher.getId();

            if (course.getPreReqCode() != null) {
                Course preReqCourse = courseRepository.findByCourseCode(course.getPreReqCode())
                        .orElse(null);

                if (preReqCourse != null) {
                    PreReqResult preReq = new PreReqResult();
                    preReq.courseCode = preReqCourse.getCourseCode();
                    preReq.courseName = preReqCourse.getCourseName();
                    preReq.creditHours = preReqCourse.getCreditHours();

                    boolean completed = false;
                    Double foundGpa = null;

                    if (student.getSemesters() != null) {
                        for (Student.Semester sem : student.getSemesters()) {
                            for (Student.Courses attempted : sem.getCourses()) {
                                if (attempted.getDetails() != null &&
                                        attempted.getDetails().getCourseCode().equalsIgnoreCase(preReqCourse.getCourseCode())) {
                                    completed = true;
                                    foundGpa = attempted.getGpa();
                                    break;
                                }
                            }
                            if (completed) break;
                        }
                    }

                    preReq.status = preReqCourse.getStatus();
                    preReq.gpa = foundGpa != null ? foundGpa : 0.0;

                    registrationForm.preReqResult = preReq;
                }
            }

            Section.Class sectionClass = section.getClasses();
            if (sectionClass.getStudents() == null) {
                sectionClass.setStudents(new ArrayList<>());
            }

            if (!sectionClass.getStudents().contains(studentId)) {
                sectionClass.getStudents().add(studentId);
            }
            sectionRepository.save(section);

            Student.Courses newCourse = new Student.Courses();
            newCourse.setCourseId(course.getId());
            newCourse.setSection(section.getId());
            newCourse.setGpa(0.0);
            newCourse.setDetails(null);

            semester.getCourses().add(newCourse);
            semester.getSections().add(section.getId());
            semester.getGpa().add(0.0);

            registration.add(registrationForm);
        }

        if (student.getSemesters() == null) {
            student.setSemesters(new ArrayList<>());
        }

        student.getSemesters().add(semester);
        studentsRepository.save(student);

        return registration;
    }

    public StudentGetResponse getStudent(String assignedEmail) {
        Student student = studentsRepository.findByAssignedEmail(assignedEmail).orElseThrow(() -> new RuntimeException("Student Not Found"));

        return new StudentGetResponse(
                String.valueOf(student.getId()),
                student.getName(),
                student.getFatherName(),
                student.getBloodGroup(),
                student.getAddress(),
                student.getPersonalEmail(),
                student.getAssignedEmail(),
                student.getPhone(),
                student.getEmergency(),
                student.getBatch(),
                student.getDegree(),
                student.getGender(),
                student.getDob(),
                student.getSemesters()
        );
    }
}