package com.example.Gradely.service;

import com.example.Gradely.database.model.Course;
import com.example.Gradely.database.model.Department;
import com.example.Gradely.database.model.Teacher;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.DepartmentsRepository;
import com.example.Gradely.database.repository.TeachersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeachersService {

    private final TeachersRepository teachersRepository;
    private final DepartmentsRepository departmentsRepository;
    private final CoursesRepository coursesRepository;
    private final PasswordEncoder passwordEncoder;

    public TeachersService(TeachersRepository teachersRepository, DepartmentsRepository departmentsRepository, CoursesRepository coursesRepository, PasswordEncoder passwordEncoder) {
        this.teachersRepository = teachersRepository;
        this.departmentsRepository = departmentsRepository;
        this.coursesRepository = coursesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static class TeacherRequest {
        public String teacherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String position;
        public String gender;
        public List<String> qualification;
        public Integer age;
        public String departmentId;
    }

    public static class TeacherLoginRequest {
        public String email;
        public String password;

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
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
                body.age
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
    public List<String> assignCoursesToTeacher(String teacherId, List<String> courseIds) {
        Teacher teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Course> courses = coursesRepository.findAllById(courseIds);

        if (courses.size() != courseIds.size()) {
            throw new RuntimeException("One or more courses not found");
        }

        List<Teacher.CourseInfo> courseInfos = courses.stream().map(course -> {
            Teacher.CourseInfo info = new Teacher.CourseInfo();
            info.setId(course.getId());
            info.setRating(0);
            info.setBestRatedComment("");
            info.setWorstRatedComment("");
            return info;
        }).toList();

        if (teacher.getCourses() == null) {
            teacher.setCourses(new ArrayList<>());
        }
        teacher.getCourses().addAll(courseInfos);

        for (Course course : courses) {
            if (course.getTeachers() == null) {
                course.setTeachers(new ArrayList<>());
            }

            Course.TeacherInfo teacherInfo = new Course.TeacherInfo();
            teacherInfo.setId(teacher.getId());
            teacherInfo.setName(teacher.getName());
            teacherInfo.setSections(new ArrayList<>());

            course.getTeachers().add(teacherInfo);
        }

        teachersRepository.save(teacher);
        coursesRepository.saveAll(courses);

        return teacher.getCourses().stream()
                .map(c -> String.valueOf(c.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeAllCoursesFromAllTeachers() {
        List<Teacher> allTeachers = teachersRepository.findAll();

        for (Teacher teacher : allTeachers) {
            List<Teacher.CourseInfo> teacherCourses = teacher.getCourses();
            if (teacherCourses != null && !teacherCourses.isEmpty()) {
                List<String> courseIds = teacherCourses.stream()
                        .map(courseInfo -> String.valueOf(courseInfo.getId()))
                        .collect(Collectors.toList());

                List<Course> courses = coursesRepository.findAllById(courseIds);

                for (Course course : courses) {
                    List<Course.TeacherInfo> teacherInfos = course.getTeachers();
                    if (teacherInfos != null) {
                        teacherInfos.removeIf(ti -> Objects.equals(ti.getId(), teacher.getId()));
                    }
                }

                coursesRepository.saveAll(courses);
            }

            teacher.setCourses(new ArrayList<>());
        }

        teachersRepository.saveAll(allTeachers);
    }
}