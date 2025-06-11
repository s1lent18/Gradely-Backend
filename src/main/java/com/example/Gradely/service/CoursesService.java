package com.example.Gradely.service;

import com.example.Gradely.database.model.Course;
import com.example.Gradely.database.model.Department;
import com.example.Gradely.database.model.Teacher;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.DepartmentsRepository;
import com.example.Gradely.database.repository.TeachersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoursesService {

    private final TeachersRepository teachersRepository;
    private final CoursesRepository coursesRepository;
    private final DepartmentsRepository departmentsRepository;

    public CoursesService(CoursesRepository coursesRepository, DepartmentsRepository departmentsRepository, TeachersRepository teachersRepository) {
        this.coursesRepository = coursesRepository;
        this.departmentsRepository = departmentsRepository;
        this.teachersRepository = teachersRepository;
    }

    public static class CourseWithTeachersResponse {
        public String courseId;
        public String courseName;
        public DepartmentInfo department;
        public String status;
        public Integer creditHour;
        public PrerequisiteCourseInfo preReqCourse;
        public List<TeacherInfo> teachers;

        public CourseWithTeachersResponse(
                String courseId,
                String courseName,
                String status,
                Integer creditHour,
                DepartmentInfo department,
                PrerequisiteCourseInfo preReqCourse,
                List<TeacherInfo> teachers
        ) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.status = status;
            this.creditHour = creditHour;
            this.department = department;
            this.preReqCourse = preReqCourse;
            this.teachers = teachers;
        }

        public static class DepartmentInfo {
            public String deptId;
            public String deptName;

            public DepartmentInfo(String deptId, String deptName) {
                this.deptId = deptId;
                this.deptName = deptName;
            }
        }

        public static class PrerequisiteCourseInfo {
            public String courseId;
            public String courseName;

            public PrerequisiteCourseInfo(String courseId, String courseName) {
                this.courseId = courseId;
                this.courseName = courseName;
            }
        }

        public static class TeacherInfo {
            public String teacherId;
            public String teacherName;
            public String email;

            public TeacherInfo(String teacherId, String teacherName, String email) {
                this.teacherId = teacherId;
                this.teacherName = teacherName;
                this.email = email;
            }
        }
    }

    public static class CourseRequest {
        public String courseCode;
        public String courseName;
        public String departmentId;
        public String status;
        public String preReqCourseId;
        public Integer creditHour;
    }

    public static class CourseResponse {
        public String courseId;
        public String courseName;
        public String status;
        public Integer creditHour;
        public String courseCode;
        public DepartmentInfo department;
        public PrerequisiteCourseInfo preReqCourse;

        public CourseResponse(String courseId, String courseCode, String courseName, String status, Integer creditHour, DepartmentInfo department, PrerequisiteCourseInfo preReqCourse) {
            this.courseId = courseId;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.status = status;
            this.creditHour = creditHour;
            this.department = department;
            this.preReqCourse = preReqCourse;
        }

        public static class DepartmentInfo {
            public String deptId;
            public String deptName;

            public DepartmentInfo(String deptId, String deptName) {
                this.deptId = deptId;
                this.deptName = deptName;
            }
        }

        public static class PrerequisiteCourseInfo {
            public String courseId;
            public String courseName;

            public PrerequisiteCourseInfo(String courseId, String courseName) {
                this.courseId = courseId;
                this.courseName = courseName;
            }
        }
    }

    public List<CourseWithTeachersResponse> getAllCoursesWithTeachers() {
        return coursesRepository.findAll().stream().map(course -> {
            Department department = departmentsRepository.findById(course.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found for course " + course.getId()));

            CourseWithTeachersResponse.DepartmentInfo dept = new CourseWithTeachersResponse.DepartmentInfo(
                    department.getId(),
                    department.getDepartmentName()
            );

            Course prereqCourse = null;
            if (course.getPreReqCode() != null) {
                prereqCourse = coursesRepository.findByCourseCode(course.getPreReqCode())
                        .orElse(null);
            }

            CourseWithTeachersResponse.PrerequisiteCourseInfo prereq = prereqCourse != null
                    ? new CourseWithTeachersResponse.PrerequisiteCourseInfo(
                    prereqCourse.getId(),
                    prereqCourse.getCourseName()
            )
                    : null;

            List<CourseWithTeachersResponse.TeacherInfo> teachers = course.getTeachers() != null
                    ? course.getTeachers().stream()
                    .map(teacherInfo -> new CourseWithTeachersResponse.TeacherInfo(
                            teacherInfo.getId(),
                            teacherInfo.getName(),
                            teacherInfo.getAssignedEmail()
                    ))
                    .toList()
                    : Collections.emptyList();

            return new CourseWithTeachersResponse(
                    course.getId(),
                    course.getCourseName(),
                    course.getStatus(),
                    course.getCreditHours(),
                    dept,
                    prereq,
                    teachers
            );
        }).toList();
    }

    @Transactional
    public CourseResponse add(CourseRequest body) {
        Department dept = departmentsRepository.findById(body.departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Course prereq = null;
        if (body.preReqCourseId != null) {
            prereq = coursesRepository.findById(body.preReqCourseId)
                    .orElseThrow(() -> new RuntimeException("Prerequisite course not found"));
        }

        Course course = prereq != null
            ? new Course(body.courseCode, body.courseName, body.creditHour, body.status, prereq.getCourseCode())
            : new Course(body.courseCode, body.courseName, body.creditHour, body.status);

        course.setDepartmentId(dept.getId());

        coursesRepository.save(course);

        return new CourseResponse(
            course.getId(),
            course.getCourseCode(),
            course.getCourseName(),
            course.getStatus(),
            course.getCreditHours(),
            new CoursesService.CourseResponse.DepartmentInfo(dept.getId(), dept.getDepartmentName()),
            prereq != null ? new CourseResponse.PrerequisiteCourseInfo(prereq.getCourseCode(), prereq.getCourseName()) : null
        );
    }

    @Transactional
    public void assignTeachersToCourse(String courseId, List<String> teacherIds, List<String> sections) {
        Course course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Teacher> teachers = teachersRepository.findAllById(teacherIds);

        if (teachers.size() != teacherIds.size()) {
            throw new RuntimeException("One or more teachers not found");
        }

        List<Course.TeacherInfo> newTeacherInfos = new ArrayList<>();
        Set<String> existingIds = course.getTeachers().stream()
                .map(Course.TeacherInfo::getId)
                .collect(Collectors.toSet());

        Queue<String> sectionQueue = new LinkedList<>(sections);

        for (Teacher teacher : teachers) {
            String teacherId = teacher.getId();

            if (existingIds.contains(teacherId)) continue;

            List<String> assignedSections = new ArrayList<>();

            if (sections.size() == teachers.size()) {
                if (!sectionQueue.isEmpty()) {
                    assignedSections.add(sectionQueue.poll());
                }
            } else {
                if ("assistant professor".equalsIgnoreCase(teacher.getPosition())) {
                    if (!sectionQueue.isEmpty()) assignedSections.add(sectionQueue.poll());
                    if (!sectionQueue.isEmpty()) assignedSections.add(sectionQueue.poll());
                } else {
                    if (!sectionQueue.isEmpty()) assignedSections.add(sectionQueue.poll());
                }
            }

            Course.TeacherInfo info = new Course.TeacherInfo(teacherId, teacher.getName(), teacher.getAssignedEmail(), assignedSections);
            newTeacherInfos.add(info);

            boolean alreadyAssigned = teacher.getCourses().stream()
                    .anyMatch(c -> String.valueOf(c.getId()).equals(course.getId()));

            if (!alreadyAssigned) {
                teacher.getCourses().add(new Teacher.CourseInfo(
                        course.getId(),
                        0,
                        "",
                        ""
                ));
            }
        }

        course.getTeachers().addAll(newTeacherInfos);
        coursesRepository.save(course);
        teachersRepository.saveAll(teachers);
    }
}