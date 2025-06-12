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
import java.util.Map;
import java.util.stream.Collectors;

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
        public String status;
        public Integer warningCount;
        public List<Student.Semester> semesters;

        public StudentGetResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String assignedEmail, String phone, String emergency, String batch, String degree, String gender, String dob, List<Student.Semester> semesters, String status, Integer warningCount) {
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
            this.status = status;
            this.warningCount = warningCount;
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
        public Integer warningCount;

        public StudentResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String phone, String emergency, String degree, String gender, String dob, String password, String assignedEmail, String batch, Integer semester, Integer warningCount) {
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
            this.warningCount = warningCount;
        }
    }

    public static class StudentRegisterRequest {
        public String courseId;
        public String sectionId;
        public String teacherId;
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
            1,
            0
        );
    }

    @Transactional
    public List<CourseRegistration> registerCourses(String studentId, List<StudentRegisterRequest> request) {
        Student student = studentsRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));

        List<String> courseIds = request.stream().map(r -> r.courseId).collect(Collectors.toList());
        List<String> sectionIds = request.stream().map(r -> r.sectionId).collect(Collectors.toList());
        List<String> teacherIds = request.stream().map(r -> r.teacherId).collect(Collectors.toList());

        Map<String, Course> courseMap = courseRepository.findAllById(courseIds)
                .stream().collect(Collectors.toMap(Course::getId, c -> c));
        Map<String, Section> sectionMap = sectionRepository.findAllById(sectionIds)
                .stream().collect(Collectors.toMap(Section::getId, s -> s));
        Map<String, Teacher> teacherMap = teachersRepository.findAllById(teacherIds)
                .stream().collect(Collectors.toMap(Teacher::getId, t -> t));

        if (courseMap.size() != courseIds.size())
            throw new RuntimeException("Some course IDs are invalid");
        if (sectionMap.size() != sectionIds.size())
            throw new RuntimeException("Some section IDs are invalid");
        if (teacherMap.size() != teacherIds.size())
            throw new RuntimeException("Some teacher IDs are invalid");

        int currentSemesterNumber = (student.getSemesters() != null && !student.getSemesters().isEmpty())
                ? student.getSemesters().get(student.getSemesters().size() - 1).getNumber() + 1
                : 1;

        Student.Semester semester = new Student.Semester();
        semester.setNumber(currentSemesterNumber);
        semester.setCourses(new ArrayList<>());
        semester.setSections(new ArrayList<>());
        semester.setGpa(new ArrayList<>());

        List<CourseRegistration> registration = new ArrayList<>();

        for (StudentRegisterRequest req : request) {
            Course course = courseMap.get(req.courseId);
            Section section = sectionMap.get(req.sectionId);
            Teacher teacher = teacherMap.get(req.teacherId);

            // Check pre-req
            PreReqResult preReq = null;
            if (course.getPreReqCode() != null) {
                Course preReqCourse = courseRepository.findByCourseCode(course.getPreReqCode()).orElse(null);

                if (preReqCourse == null) {
                    throw new RuntimeException("Pre-requisite course not found for: " + course.getCourseCode());
                }

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

                if (!completed) {
                    throw new RuntimeException("Cannot register for " + course.getCourseCode()
                            + ". Prerequisite " + preReqCourse.getCourseCode() + " has not been completed.");
                }

                // ✅ Set preReqResult
                preReq = new PreReqResult();
                preReq.courseCode = preReqCourse.getCourseCode();
                preReq.courseName = preReqCourse.getCourseName();
                preReq.creditHours = preReqCourse.getCreditHours();
                preReq.status = preReqCourse.getStatus();
                preReq.gpa = foundGpa != null ? foundGpa : 0.0;
            }

            // Register the student in the class
            Section.Class matchingClass = getAClass(section, course, teacher);

            if (matchingClass.getStudentAttendance() == null) {
                matchingClass.setStudentAttendance(new ArrayList<>());
            }

            boolean alreadyExists = matchingClass.getStudentAttendance().stream()
                    .anyMatch(sa -> sa.getStudent().equals(studentId));

            if (!alreadyExists) {
                Section.StudentAttendance sa = new Section.StudentAttendance();
                sa.setStudent(studentId);
                sa.setAttendance(new ArrayList<>());
                matchingClass.getStudentAttendance().add(sa);
            }

            // Create registration form
            CourseRegistration registrationForm = new CourseRegistration();
            registrationForm.courseCode = course.getCourseCode();
            registrationForm.courseName = course.getCourseName();
            registrationForm.creditHours = course.getCreditHours();
            registrationForm.status = course.getStatus();
            registrationForm.section = section.getId();
            registrationForm.teacher = teacher.getId();
            registrationForm.preReqResult = preReq; 

            sectionRepository.save(section);

            // Add to student semester
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

    private Section.Class getAClass(Section section, Course course, Teacher teacher) {
        if (section.getClasses() == null || section.getClasses().isEmpty()) {
            throw new RuntimeException("No classes defined in section " + section.getId());
        }

        return section.getClasses().stream()
                .filter(cls -> course.getId().equals(cls.getCourse()) &&
                        teacher.getId().equals(cls.getTeacher()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No matching class found in section "
                        + section.getId() + " for course " + course.getCourseCode()));
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
                student.getSemesters(),
                student.getStatus(),
                student.getWarningCount()
        );
    }
}