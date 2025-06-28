package com.example.Gradely.service;

import com.example.Gradely.database.model.*;
import com.example.Gradely.database.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentsRepository studentsRepository;
    private final CoursesRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final TeachersRepository teachersRepository;
    private final RegistrationsRepository registrationsRepository;

    public StudentService(
            StudentsRepository studentsRepository,
            CoursesRepository courseRepository,
            SectionRepository sectionRepository,
            TeachersRepository teachersRepository,
            RegistrationsRepository registrationsRepository
    ) {
        this.studentsRepository = studentsRepository;
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.teachersRepository = teachersRepository;
        this.registrationsRepository = registrationsRepository;
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

    @Getter @Setter
    public static class CourseRegistration {
        public String section;
        public String teacher;
        public String courseCode;
        public String courseName;
        public Integer creditHours;
        public String status;
        public PreReqResult preReqResult;
    }

    @Getter @Setter
    public static class PreReqResult {
        public String courseCode;
        public String courseName;
        public Integer creditHours;
        public String status;
        public Double gpa;
        public String grade;
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
        public Integer creditsAttempted;
        public Integer creditsEarned;
        public Double cgpa;
        public List<Student.Semester> semesters;

        public StudentGetResponse(String studentId, String studentName, String fatherName, String bloodGroup, String address, String personalEmail, String assignedEmail, String phone, String emergency, String batch, String degree, String gender, String dob, String status, Integer warningCount, Integer creditsAttempted, Integer creditsEarned, Double cgpa, List<Student.Semester> semesters) {
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
            this.creditsAttempted = creditsAttempted;
            this.creditsEarned = creditsEarned;
            this.cgpa = cgpa;
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

    public static class StudentRegisterPart {
        public String courseId;
        public String sectionId;
        public String teacherId;
    }

    public static class StudentRegisterRequest {
        public String semester;
        public List<StudentRegisterPart> parts;
    }

    public static class StudentAllResultRequest {
        public String Id;
        public String courseId;
    }

    @Getter @Setter
    public static class Details {
        private String courseCode;
        private String name;
        private Integer creditCount;
        private List<Student.Assignment> assignments;
        private List<Student.Quiz> quizzes;
        private Student.Exam mid1;
        private Student.Exam mid2;
        private String projectScore;
        private String projectTotal;
        private String classParticipationScore;
        private String classParticipationTotal;
        private Student.Exam finalExam;
        private String studentId;

        public Details(String studentId, Student.Course details) {
            this.studentId = studentId;
            this.courseCode = details.getCourseCode();
            this.creditCount = details.getCreditCount();
            this.name = details.getName();
            this.assignments = details.getAssignments();
            this.quizzes = details.getQuizzes();
            this.mid1 = details.getMid1();
            this.mid2 = details.getMid2();
            this.finalExam = details.getFinalExam();
            this.projectScore = details.getProjectScore();
            this.projectTotal = details.getProjectTotal();
            this.classParticipationScore = details.getClassParticipationScore();
            this.classParticipationTotal = details.getClassParticipationTotal();
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
            1,
            0
        );
    }

    @Transactional
    public List<CourseRegistration> registerCourses(String studentId, StudentRegisterRequest request) {
        Student student = studentsRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));

        List<StudentRegisterPart> parts = request.parts;

        List<String> courseIds = parts.stream().map(p -> p.courseId).collect(Collectors.toList());
        List<String> sectionIds = parts.stream().map(p -> p.sectionId).collect(Collectors.toList());
        List<String> teacherIds = parts.stream().map(p -> p.teacherId).collect(Collectors.toList());

        Map<String, Course> courseMap = courseRepository.findAllById(courseIds)
                .stream().collect(Collectors.toMap(Course::getId, c -> c));
        Map<String, Sections> sectionMap = sectionRepository.findAllById(sectionIds)
                .stream().collect(Collectors.toMap(Sections::getId, s -> s));
        Map<String, Teacher> teacherMap = teachersRepository.findAllById(teacherIds)
                .stream().collect(Collectors.toMap(Teacher::getId, t -> t));

        if (courseMap.size() != courseIds.size())
            throw new RuntimeException("Some course IDs are invalid");
        if (sectionMap.size() != sectionIds.size())
            throw new RuntimeException("Some section IDs are invalid");
        if (teacherMap.size() != teacherIds.size())
            throw new RuntimeException("Some teacher IDs are invalid");

        // Try to find existing semester by name
        String semesterName = request.semester;
        Student.Semester semester = null;

        if (student.getSemesters() != null) {
            for (Student.Semester sem : student.getSemesters()) {
                if (semesterName.equalsIgnoreCase(sem.getName())) {
                    semester = sem;
                    break;
                }
            }
        }

        boolean newSemesterCreated = false;

        if (semester == null) {
            semester = new Student.Semester();
            semester.setName(semesterName);
            semester.setCourses(new ArrayList<>());

            int nextSemesterNumber = (student.getSemesters() != null && !student.getSemesters().isEmpty())
                    ? student.getSemesters().get(student.getSemesters().size() - 1).getNumber() + 1
                    : 1;

            semester.setNumber(nextSemesterNumber);
            newSemesterCreated = true;
        }

        List<CourseRegistration> registration = new ArrayList<>();

        for (StudentRegisterPart req : parts) {
            Course course = courseMap.get(req.courseId);
            Sections sections = sectionMap.get(req.sectionId);
            Teacher teacher = teacherMap.get(req.teacherId);

            boolean alreadyRegistered = semester.getCourses().stream()
                    .anyMatch(c -> c.getCourseId().equals(req.courseId));

            if (alreadyRegistered) {
                continue; // or update section/teacher if needed
            }

            // Prerequisite validation
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

                preReq = new PreReqResult();
                preReq.courseCode = preReqCourse.getCourseCode();
                preReq.courseName = preReqCourse.getCourseName();
                preReq.creditHours = preReqCourse.getCreditHours();
                preReq.status = preReqCourse.getStatus();
                preReq.gpa = foundGpa != null ? foundGpa : 0.0;
            }

            // Class attendance setup
            Sections.Class matchingClass = getAClass(sections, course, teacher);
            if (matchingClass.getStudentAttendance() == null) {
                matchingClass.setStudentAttendance(new ArrayList<>());
            }

            boolean alreadyExists = matchingClass.getStudentAttendance().stream()
                    .anyMatch(sa -> sa.getStudent().equals(studentId));

            if (!alreadyExists) {
                Sections.StudentAttendance sa = new Sections.StudentAttendance();
                sa.setStudent(studentId);
                sa.setAttendance(new ArrayList<>());
                matchingClass.getStudentAttendance().add(sa);
            }

            // Teacher Info update
            List<Course.TeacherInfo> teacherInfos = course.getTeachers();
            if (teacherInfos == null) {
                teacherInfos = new ArrayList<>();
                course.setTeachers(teacherInfos);
            }

            Course.TeacherInfo existingTeacherInfo = teacherInfos.stream()
                    .filter(ti -> Objects.equals(ti.getId(), teacher.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingTeacherInfo == null) {
                Course.TeacherInfo info = new Course.TeacherInfo();
                info.setId(teacher.getId());
                info.setName(teacher.getName());
                info.setAssignedEmail(teacher.getAssignedEmail());
                info.setSections(new ArrayList<>(List.of(sections.getId())));
                teacherInfos.add(info);
            } else if (!existingTeacherInfo.getSections().contains(sections.getId())) {
                existingTeacherInfo.getSections().add(sections.getId());
            }

            sectionRepository.save(sections);
            courseRepository.save(course);

            // Register course in semester
            Student.Courses newCourse = new Student.Courses();
            newCourse.setCourseId(course.getId());
            newCourse.setSection(sections.getId());
            newCourse.setGpa(0.0);
            newCourse.setGrade("");
            newCourse.setAttendance(new ArrayList<>());
            newCourse.setSavePoints(new ArrayList<>());
            newCourse.setDetails(new Student.Course(course.getCourseCode(), course.getCourseName(), course.getCreditHours()));

            semester.getCourses().add(newCourse);

            // Return for response
            CourseRegistration registrationForm = new CourseRegistration();
            registrationForm.courseCode = course.getCourseCode();
            registrationForm.courseName = course.getCourseName();
            registrationForm.creditHours = course.getCreditHours();
            registrationForm.status = course.getStatus();
            registrationForm.section = sections.getId();
            registrationForm.teacher = teacher.getId();
            registrationForm.preReqResult = preReq;
            registration.add(registrationForm);
        }

        // Only add the semester if new
        if (newSemesterCreated) {
            if (student.getSemesters() == null) {
                student.setSemesters(new ArrayList<>());
            }
            student.getSemesters().add(semester);
        }

        studentsRepository.save(student);
        return registration;
    }

    private Sections.Class getAClass(Sections sections, Course course, Teacher teacher) {
        if (sections.getClasses() == null || sections.getClasses().isEmpty()) {
            throw new RuntimeException("No classes defined in section " + sections.getId());
        }

        return sections.getClasses().stream()
                .filter(cls -> course.getId().equals(cls.getCourse()) &&
                        teacher.getId().equals(cls.getTeacher()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No matching class found in section "
                        + sections.getId() + " for course " + course.getCourseCode()));
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
                student.getStatus(),
                student.getWarningCount(),
                student.getChAttempted(),
                student.getChCleared(),
                student.getCgpa(),
                student.getSemesters()
        );
    }

    @Cacheable(value = "courseRegistration", key = "#studentId", unless = "#result == null")
    public List<AdminService.CourseRegistrationInit> allowCourseRegistration(String studentId) {

        List<AdminService.CourseRegistrationInit> availableCourses = registrationsRepository
                .findFirstByOrderByCreatedAtDesc()
                .map(Registrations::getAvailableCourses)
                .orElseThrow(() -> new RuntimeException("No registration options available"));

        Student student = studentsRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Map<String, Double> courseGpaMap = new HashMap<>();
        for (Student.Semester semester : student.getSemesters()) {
            for (Student.Courses course : semester.getCourses()) {
                if (course.getCourseId() != null) {
                    courseGpaMap.put(course.getCourseId(), course.getGpa());
                }
            }
        }

        for (AdminService.CourseRegistrationInit course : availableCourses) {
            Double gpa = courseGpaMap.get(course.courseId);

            if (gpa != null) {
                if (gpa == 4.0) {
                    course.desc = "Don't Allow";
                } else {
                    course.desc = "Can Improve";
                }
            } else {
                course.desc = "Recommended - In Line with RoadMap";
            }
        }

        return availableCourses;
    }

    public List<Student.Semester> getSemesters(String studentId) {
        Student student = studentsRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student Not Found"));

        return student.getSemesters();
    }

    public List<Details> getAllResults(StudentAllResultRequest request) {
        List<String> studentIds = new ArrayList<>();

        Sections sections = sectionRepository.findById(request.Id)
                .orElseThrow(() -> new RuntimeException("Section Not Found"));

        for (Sections.Class sec : sections.getClasses()) {
            if (sec.getCourse().equals(request.courseId)) {
                for (Sections.StudentAttendance sa : sec.getStudentAttendance()) {
                    studentIds.add(sa.getStudent());
                }
            }
        }

        List<Student> students = studentsRepository.findAllById(studentIds);
        List<Details> results = new ArrayList<>();

        for (Student student : students) {
            for (Student.Semester semester : student.getSemesters()) {
                for (Student.Courses course : semester.getCourses()) {
                    if (course.getCourseId().equals(request.courseId)) {
                        if (course.getDetails() != null) {
                            results.add(new Details(student.getId(), course.getDetails()));
                        }
                    }
                }
            }
        }

        return results;
    }
}