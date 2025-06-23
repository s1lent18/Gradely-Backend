package com.example.Gradely.service;

import com.example.Gradely.database.model.Course;
import com.example.Gradely.database.model.Department;
import com.example.Gradely.database.model.Section;
import com.example.Gradely.database.model.Teacher;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.DepartmentsRepository;
import com.example.Gradely.database.repository.SectionRepository;
import com.example.Gradely.database.repository.TeachersRepository;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TeachersService {

    private final TeachersRepository teachersRepository;
    private final DepartmentsRepository departmentsRepository;
    private final CoursesRepository coursesRepository;
    private final PasswordEncoder passwordEncoder;
    private final SectionRepository sectionRepository;

    public TeachersService(TeachersRepository teachersRepository, DepartmentsRepository departmentsRepository, CoursesRepository coursesRepository, PasswordEncoder passwordEncoder, SectionRepository sectionRepository) {
        this.teachersRepository = teachersRepository;
        this.departmentsRepository = departmentsRepository;
        this.coursesRepository = coursesRepository;
        this.passwordEncoder = passwordEncoder;
        this.sectionRepository = sectionRepository;
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
        }
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
    public Section assignCourseToTeacher(String sectionId, String teacherId, String courseId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Teacher teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (section.getClasses() == null) {
            section.setClasses(new ArrayList<>());
        }

        boolean alreadyAssigned = section.getClasses().stream()
                .anyMatch(cls -> courseId.equals(cls.getCourse()) && teacherId.equals(cls.getTeacher()));

        if (!alreadyAssigned) {
            Section.Class sectionClass = new Section.Class();
            sectionClass.setTeacher(teacherId);
            sectionClass.setCourse(courseId);
            sectionClass.setStudentAttendance(new ArrayList<>());

            section.getClasses().add(sectionClass);
        }

        Optional<Teacher.Section> teacherSectionOpt = teacher.getSections().stream()
                .filter(sec -> sec.getName().equalsIgnoreCase(section.getName()))
                .findFirst();

        Teacher.CourseInfo courseInfo = new Teacher.CourseInfo(courseId, 0.0, "", "");

        if (teacherSectionOpt.isPresent()) {
            Teacher.Section teacherSection = teacherSectionOpt.get();
            boolean courseExists = teacherSection.getCourse().stream()
                    .anyMatch(c -> c.getId().equals(courseId));
            if (!courseExists) {
                teacherSection.getCourse().add(courseInfo);
            }
        } else {
            Teacher.Section newSection = new Teacher.Section();
            newSection.setName(section.getName());
            newSection.setCourse(new ArrayList<>(List.of(courseInfo)));
            teacher.getSections().add(newSection);
        }

        sectionRepository.save(section);
        teachersRepository.save(teacher);

        return section;
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
}