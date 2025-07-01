package com.example.Gradely.service;

import com.example.Gradely.database.model.*;
import com.example.Gradely.database.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TeachersService {

    private final StudentsRepository studentsRepository;
    private final TeachersRepository teachersRepository;
    private final DepartmentsRepository departmentsRepository;
    private final CoursesRepository coursesRepository;
    private final PasswordEncoder passwordEncoder;
    private final SectionRepository sectionRepository;

    public TeachersService(TeachersRepository teachersRepository,
                           DepartmentsRepository departmentsRepository,
                           CoursesRepository coursesRepository,
                           PasswordEncoder passwordEncoder,
                           SectionRepository sectionRepository,
                           StudentsRepository studentsRepository
    ) {
        this.teachersRepository = teachersRepository;
        this.departmentsRepository = departmentsRepository;
        this.coursesRepository = coursesRepository;
        this.passwordEncoder = passwordEncoder;
        this.sectionRepository = sectionRepository;
        this.studentsRepository = studentsRepository;
    }

    public static class TeacherRequest {
        public String teacherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String gender;
        public List<String> qualification;
        public String dob;
        public String departmentId;
    }

    @Getter
    public static class TeacherLoginRequest {
        public String email;
        public String password;
    }

    @Getter @Setter
    public static class TeacherMarkingStudent {
        public String studentId;
        public TeacherMarking marking;
    }

    @Getter @Setter
    public static class TeacherMarkingRequest {
        public String courseId;
        public String sectionId;
        public List<TeacherMarkingStudent> markingStudents;
    }

    @Getter @Setter
    public static class TeacherMarking {
        public List<Student.Assignment> assignments;
        public List<Student.Quiz> quizzes;
        public Student.Exam mid1;
        public Student.Exam mid2;
        public String projectScore;
        public String projectTotal;
        public String classParticipationScore;
        public String classParticipationTotal;
        public Student.Exam finalExam;

        public TeacherMarking(List<Student.Assignment> assignments, List<Student.Quiz> quizzes,
                              Student.Exam mid1, Student.Exam mid2, String projectScore, String projectTotal,
                              String classParticipationScore, String classParticipationTotal, Student.Exam finalExam
        ) {
            this.assignments = assignments;
            this.quizzes = quizzes;
            this.mid1 = mid1;
            this.mid2 = mid2;
            this.projectScore = projectScore;
            this.projectTotal = projectTotal;
            this.classParticipationScore = classParticipationScore;
            this.classParticipationTotal = classParticipationTotal;
            this.finalExam = finalExam;
        }
    }

    public static class TeachersResponse {
        public String teacherId;
        public String teacherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String position;
        public List<String> qualification;
        public String gender;
        public String password;
        public String assignedEmail;
        public DepartmentInfo department;

        public TeachersResponse(String teacherId, String teacherName, String bloodGroup, String address,
                                String personalEmail, String phone, String emergency, String position, List<String> qualification,
                                String gender, String password, String assignedEmail, DepartmentInfo department) {
            this.teacherId = teacherId;
            this.teacherName = teacherName;
            this.bloodGroup = bloodGroup;
            this.address = address;
            this.personalEmail = personalEmail;
            this.phone = phone;
            this.emergency = emergency;
            this.position = position;
            this.qualification = qualification;
            this.gender = gender;
            this.password = password;
            this.assignedEmail = assignedEmail;
            this.department = department;
        }

        public static class DepartmentInfo {
            public String deptId;
            public String deptName;

            public DepartmentInfo(String deptId, String deptName) {
                this.deptId = deptId;
                this.deptName = deptName;
            }
        }
    }

    public static class TeachersGetResponse {
        public String token;
        public String teacherId;
        public String teacherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String position;
        public List<String> qualification;
        public String gender;
        public String status;
        public String dob;
        public String hiringYear;
        public String assignedEmail;
        public List<Teacher.Section> sections;
        public TeachersService.TeachersResponse.DepartmentInfo department;

        public TeachersGetResponse(String teacherId, String teacherName, String bloodGroup, String address,
                                String personalEmail, String phone, String emergency, String position, List<String> qualification,
                                String gender, String assignedEmail, TeachersService.TeachersResponse.DepartmentInfo department,
                                   List<Teacher.Section> sections, String status, String dob, String hiringYear) {
            this.teacherId = teacherId;
            this.teacherName = teacherName;
            this.bloodGroup = bloodGroup;
            this.address = address;
            this.personalEmail = personalEmail;
            this.phone = phone;
            this.emergency = emergency;
            this.position = position;
            this.qualification = qualification;
            this.gender = gender;
            this.assignedEmail = assignedEmail;
            this.department = department;
            this.sections = sections;
            this.status = status;
            this.dob = dob;
            this.hiringYear = hiringYear;
        }
    }

    public static class TeacherAttendanceRequest {
        public String sectionName;
        public String courseId;
        public List<TeacherAttendancePart> attendanceParts;
    }

    public static class TeacherAttendancePart {
        public String studentId;
        public List<Student.Attendance> attendances;
    }

    public TeachersGetResponse getTeacher(String assignedEmail) {
        Teacher teacher = teachersRepository.findByAssignedEmail(assignedEmail).orElseThrow(() -> new RuntimeException("Teacher Not Found"));
        Department dept = departmentsRepository.findById(teacher.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));

        return new TeachersGetResponse(
                String.valueOf(teacher.getId()),
                teacher.getName(),
                teacher.getBloodGroup(),
                teacher.getAddress(),
                teacher.getPersonalEmail(),
                teacher.getPhone(),
                teacher.getEmergency(),
                teacher.getPosition(),
                teacher.getQualification(),
                teacher.getGender(),
                teacher.getAssignedEmail(),
                new TeachersResponse.DepartmentInfo(dept.getId(), dept.getDepartmentName()),
                teacher.getSections(),
                teacher.getStatus(),
                teacher.getDob(),
                teacher.getHiringYear()
        );
    }

    @Transactional
    public TeachersService.TeachersResponse add(TeachersService.TeacherRequest body) {
        Department dept = departmentsRepository.findById(body.departmentId).orElseThrow(() -> new RuntimeException("Department not found"));

        Teacher teacher = new Teacher(
                body.teacherName,
                body.personalEmail,
                body.bloodGroup,
                body.address,
                body.phone,
                body.emergency,
                "",
                body.qualification,
                body.gender,
                body.dob
        );

        teacher.setDepartmentId(body.departmentId);

        Teacher savedTeacher = teachersRepository.save(teacher);

        String assignedEmail = body.teacherName.replaceAll("\\s+", ".").toLowerCase() + "." + "@unifaculty.com";

        savedTeacher.setAssignedEmail(assignedEmail);

        String qualification = body.qualification.get(0).toLowerCase();

        if (qualification.contains("phd")) {
            savedTeacher.setPosition("Assistant Professor");
        } else {
            savedTeacher.setPosition("Assistant Lecturer");
        }

        String rawPassword = savedTeacher.getPassword();

        savedTeacher.setPassword(passwordEncoder.encode(savedTeacher.getPassword()));

        teachersRepository.save(savedTeacher);

        return new TeachersResponse(
                savedTeacher.getId(),
                savedTeacher.getName(),
                savedTeacher.getBloodGroup(),
                savedTeacher.getAddress(),
                savedTeacher.getPersonalEmail(),
                savedTeacher.getPhone(),
                savedTeacher.getEmergency(),
                savedTeacher.getPosition(),
                savedTeacher.getQualification(),
                savedTeacher.getGender(),
                rawPassword,
                savedTeacher.getAssignedEmail(),
                new TeachersResponse.DepartmentInfo(dept.getId(), dept.getDepartmentName())
        );
    }

    @Transactional
    public Sections assignCourseToTeacher(String sectionId, String teacherId, String courseId) {
        Sections sections = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Teacher teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (sections.getClasses() == null) {
            sections.setClasses(new ArrayList<>());
        }

        boolean alreadyAssigned = sections.getClasses().stream()
                .anyMatch(cls -> courseId.equals(cls.getCourse()) && teacherId.equals(cls.getTeacher()));

        if (!alreadyAssigned) {
            Sections.Class sectionClass = new Sections.Class();
            sectionClass.setTeacher(teacherId);
            sectionClass.setCourse(courseId);
            sectionClass.setStudentAttendance(new ArrayList<>());

            sections.getClasses().add(sectionClass);
        }

        Optional<Teacher.Section> teacherSectionOpt = teacher.getSections().stream()
                .filter(sec -> sec.getName().equalsIgnoreCase(sections.getName()))
                .findFirst();

        Teacher.CourseInfo courseInfo = new Teacher.CourseInfo(courseId, course.getCourseName(), 0.0, "", "", 0);

        if (teacherSectionOpt.isPresent()) {
            Teacher.Section teacherSection = teacherSectionOpt.get();
            boolean courseExists = teacherSection.getCourse().stream()
                    .anyMatch(c -> c.getId().equals(courseId));
            if (!courseExists) {
                teacherSection.getCourse().add(courseInfo);
            }
        } else {
            Teacher.Section newSection = new Teacher.Section();
            newSection.setName(sections.getName());
            newSection.setCourse(new ArrayList<>(List.of(courseInfo)));
            teacher.getSections().add(newSection);
        }

        sectionRepository.save(sections);
        teachersRepository.save(teacher);

        return sections;
    }

    @Transactional
    public void removeAllCoursesFromAllTeachers() {
        List<Teacher> allTeachers = teachersRepository.findAll();

        for (Teacher teacher : allTeachers) {
            Set<String> courseIds = new HashSet<>();

            for (Teacher.Section section : teacher.getSections()) {
                for (Teacher.CourseInfo courseInfo : section.getCourse()) {
                    courseIds.add(courseInfo.getId());
                }
            }

            List<Course> courses = coursesRepository.findAllById(courseIds.stream().toList());

            for (Course course : courses) {
                if (course.getTeachers() != null) {
                    course.getTeachers().removeIf(ti -> Objects.equals(ti.getId(), teacher.getId()));
                }
            }

            coursesRepository.saveAll(courses);
            teacher.setSections(new ArrayList<>());
        }

        teachersRepository.saveAll(allTeachers);
    }

    @Transactional
    public TeacherMarkingRequest markStudents(String teacherId, TeacherMarkingRequest marking) {
        teachersRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher Not Found"));

        List<String> studentIds = marking.getMarkingStudents().stream()
                .map(TeacherMarkingStudent::getStudentId).toList();

        List<Student> students = studentsRepository.findAllById(studentIds);
        if (students.size() != studentIds.size()) {
            throw new RuntimeException("Some student IDs are invalid");
        }

        Course course = coursesRepository.findById(marking.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        for (TeacherMarkingStudent mStudent : marking.getMarkingStudents()) {
            String studentId = mStudent.getStudentId();
            TeacherMarking mark = mStudent.getMarking();

            Student student = students.stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Student not found in list"));

            for (Student.Semester semester : student.getSemesters()) {
                for (Student.Courses courseEntry : semester.getCourses()) {
                    if (!courseEntry.getCourseId().equals(marking.getCourseId())) continue;

                    Student.Course details = courseEntry.getDetails();
                    if (details == null) {
                        details = new Student.Course(course.getCourseCode(), course.getCourseName(), course.getCreditHours());
                        courseEntry.setDetails(details);
                    }

                    if (mark.getAssignments() != null) {
                        List<Student.Assignment> assignments = details.getAssignments();
                        if (assignments == null) {
                            assignments = new ArrayList<>();
                        }
                        assignments.addAll(mark.getAssignments());
                        details.setAssignments(assignments);
                    }

                    if (mark.getQuizzes() != null) {
                        List<Student.Quiz> quizzes = details.getQuizzes();
                        if (quizzes == null) {
                            quizzes = new ArrayList<>();
                        }
                        quizzes.addAll(mark.getQuizzes());
                        details.setQuizzes(quizzes);
                    }
                    if (mark.getMid1() != null) {
                        details.setMid1(mark.getMid1());
                    }
                    if (mark.getMid2() != null) {
                        details.setMid2(mark.getMid2());
                    }
                    if (mark.getFinalExam() != null) {
                        details.setFinalExam(mark.getFinalExam());
                    }
                    if (mark.getProjectScore() != null && mark.getProjectTotal() != null) {
                        details.setProjectScore(mark.getProjectScore());
                        details.setProjectTotal(mark.getProjectTotal());
                    }
                    if (mark.getClassParticipationScore() != null && mark.getClassParticipationTotal() != null) {
                        details.setClassParticipationScore(mark.getClassParticipationScore());
                        details.setClassParticipationTotal(mark.getClassParticipationTotal());
                    }

                    double total = getTotal(details);
                    System.out.println(total);
                    double sum = getSum(details);
                    System.out.println(sum);

                    List<String> temp = courseEntry.getSavePoints();
                    if (temp == null) {
                        temp = new ArrayList<>();
                    }
                    temp.add(String.valueOf(sum));
                    courseEntry.setSavePoints(temp);

                    double gpa =
                            (sum >= 86) ? 4.00 :
                            (sum >= 82) ? 3.67 :
                            (sum >= 78) ? 3.33 :
                            (sum >= 74) ? 3.00 :
                            (sum >= 70) ? 2.67 :
                            (sum >= 66) ? 2.33 :
                            (sum >= 62) ? 2.00 :
                            (sum >= 58) ? 1.67 :
                            (sum >= 54) ? 1.33 :
                            (sum >= 50) ? 1.00 :
                            0.00;

                    String grade =
                            (sum >= 90) ? "A+":
                            (sum >= 86) ? "A" :
                            (sum >= 82) ? "A-" :
                            (sum >= 78) ? "B+" :
                            (sum >= 74) ? "B" :
                            (sum >= 70) ? "B-" :
                            (sum >= 66) ? "C+" :
                            (sum >= 62) ? "C" :
                            (sum >= 58) ? "C-" :
                            (sum >= 54) ? "D+" :
                            (sum >= 50) ? "D" :
                            "F";

                    if (total == 100) {
                        courseEntry.setGpa(gpa);
                        courseEntry.setGrade(grade);
                    }
                }
            }
        }

        studentsRepository.saveAll(students);
        return marking;
    }

    private static double safeParse(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            // Optionally log the error
            return 0.0;
        }
    }

    private static double getTotal(Student.Course details) {
        double summ = 0.0;

        if (details.getAssignments() != null) {
            for (Student.Assignment assignment : details.getAssignments()) {
                summ += safeParse(assignment.getWeightage());
            }
        }

        System.out.println(summ);

        if (details.getQuizzes() != null) {
            for (Student.Quiz quiz : details.getQuizzes()) {
                summ += safeParse(quiz.getWeightage());
            }
        }

        System.out.println(summ);

        summ += safeParse(details.getMid1().getWeightage());
        System.out.println(summ);
        summ += safeParse(details.getClassParticipationTotal());
        System.out.println(summ);
        summ += safeParse(details.getProjectTotal());
        System.out.println(summ);
        summ += safeParse(details.getMid2().getWeightage());
        System.out.println(summ);
        summ += safeParse(details.getFinalExam().getWeightage());
        System.out.println(summ);

        return summ;
    }

    private static double getSum(Student.Course details) {
        double summ = 0.0;

        if (details.getAssignments() != null) {
            for (Student.Assignment assignment : details.getAssignments()) {
                summ += safeParse(assignment.getWeightage());
            }
        }

        if (details.getQuizzes() != null) {
            for (Student.Quiz quiz : details.getQuizzes()) {
                summ += safeParse(quiz.getWeightage());
            }
        }

        summ += calculateExamScore(details.getMid1());
        summ += calculateExamScore(details.getMid2());
        summ += calculateExamScore(details.getFinalExam());

        summ += safeParse(details.getClassParticipationScore());
        summ += safeParse(details.getProjectScore());

        return summ;
    }

    private static double calculateExamScore(Student.Exam exam) {
        if (exam == null) return 0.0;

        try {
            double score = Double.parseDouble(exam.getExamScore());
            double total = Double.parseDouble(exam.getExamTotal());
            double weight = Double.parseDouble(exam.getWeightage());

            if (total == 0) return 0.0; // Avoid division by zero

            return (score / total) * weight;
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Transactional
    public TeacherAttendanceRequest markAttendance(String teacherId, TeacherAttendanceRequest body) {
        Sections section = sectionRepository.findByName(body.sectionName)
                .orElseThrow(() -> new RuntimeException("Section Not Found"));

        List<Sections.Class> classes = section.getClasses();
        Map<String, Student> studentMap = new HashMap<>();

        for (Sections.Class cls : classes) {
            if (Objects.equals(teacherId, cls.getTeacher()) && Objects.equals(body.courseId, cls.getCourse())) {
                List<Sections.StudentAttendance> studentAttendances = cls.getStudentAttendance();

                for (TeacherAttendancePart part : body.attendanceParts) {
                    String studentId = part.studentId;
                    List<Student.Attendance> newAttendance = part.attendances;


                    for (Sections.StudentAttendance existing : studentAttendances) {
                        if (existing.getStudent().equals(studentId)) {
                            List<Sections.AttendanceEntry> attendanceEntries = existing.getAttendance();
                            if (attendanceEntries == null) {
                                attendanceEntries = new ArrayList<>();
                                existing.setAttendance(attendanceEntries);
                            }

                            for (Student.Attendance att : newAttendance) {
                                Sections.AttendanceEntry entry = new Sections.AttendanceEntry();
                                entry.setDate(att.getDate());
                                entry.setStatus(att.getStatus());
                                attendanceEntries.add(entry);
                            }
                            break;
                        }
                    }

                    Student student = studentMap.computeIfAbsent(studentId,
                            id -> studentsRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Student not found: " + id)));

                    List<Student.Semester> semesters = student.getSemesters();
                    if (semesters != null) {

                        for (int i = semesters.size() - 1; i >= 0; i--) {
                            Student.Semester semester = semesters.get(i);
                            for (Student.Courses course : semester.getCourses()) {
                                if (course.getCourseId().equals(body.courseId)) {

                                    List<Student.Attendance> studentAttendance = course.getAttendance();
                                    if (studentAttendance == null) {
                                        studentAttendance = new ArrayList<>();
                                        course.setAttendance(studentAttendance);
                                    }

                                    studentAttendance.addAll(newAttendance);
                                    break;
                                }
                            }
                        }
                    }
                }

                break;
            }
        }

        sectionRepository.save(section);
        studentsRepository.saveAll(studentMap.values());

        return body;
    }
}