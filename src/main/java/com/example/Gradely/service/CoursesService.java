package com.example.Gradely.service;

import com.example.Gradely.database.model.Courses;
import com.example.Gradely.database.model.Departments;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.DepartmentsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoursesService {

    private final CoursesRepository coursesRepository;
    private final DepartmentsRepository departmentsRepository;

    public CoursesService(CoursesRepository coursesRepository, DepartmentsRepository departmentsRepository) {
        this.coursesRepository = coursesRepository;
        this.departmentsRepository = departmentsRepository;
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
            public Long deptId;
            public String deptName;

            public DepartmentInfo(Long deptId, String deptName) {
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
            public Long teacherId;
            public String teacherName;
            public String email;

            public TeacherInfo(Long teacherId, String teacherName, String email) {
                this.teacherId = teacherId;
                this.teacherName = teacherName;
                this.email = email;
            }
        }
    }

    public static class CourseRequest {
        public String courseId;
        public String courseName;
        public Long departmentId;
        public String status;
        public String preReqCourseId;
        public Integer creditHour;
    }

    public static class CourseResponse {
        public String courseId;
        public String courseName;
        public String status;
        public Integer creditHour;
        public DepartmentInfo department;
        public PrerequisiteCourseInfo preReqCourse;

        public CourseResponse(String courseId, String courseName, String status, Integer creditHour, DepartmentInfo department, PrerequisiteCourseInfo preReqCourse) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.status = status;
            this.creditHour = creditHour;
            this.department = department;
            this.preReqCourse = preReqCourse;
        }

        public static class DepartmentInfo {
            public Long deptId;
            public String deptName;

            public DepartmentInfo(Long deptId, String deptName) {
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
            CourseWithTeachersResponse.DepartmentInfo dept = new CourseWithTeachersResponse.DepartmentInfo(
                    course.getDepartment().getDeptId(),
                    course.getDepartment().getDeptName()
            );

            CourseWithTeachersResponse.PrerequisiteCourseInfo prereq = course.getPrereqCourse() != null
                    ? new CourseWithTeachersResponse.PrerequisiteCourseInfo(
                    course.getPrereqCourse().getCourseId(),
                    course.getPrereqCourse().getCourseName()
            )
                    : null;

            List<CourseWithTeachersResponse.TeacherInfo> teachers = course.getTeachers().stream()
                    .map(teacher -> new CourseWithTeachersResponse.TeacherInfo(
                            teacher.getTeacherId(),
                            teacher.getTeacherName(),
                            teacher.getAssignedEmail()
                    )).toList();

            return new CourseWithTeachersResponse(
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getStatus(),
                    course.getCreditHour(),
                    dept,
                    prereq,
                    teachers
            );
        }).toList();
    }

    @Transactional
    public CourseResponse add(CourseRequest body) {
        Departments dept = departmentsRepository.findById(body.departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Courses prereq = null;
        if (body.preReqCourseId != null) {
            prereq = coursesRepository.findById(body.preReqCourseId)
                    .orElseThrow(() -> new RuntimeException("Prerequisite course not found"));
        }

        Courses course = prereq != null
                ? new Courses(body.courseId, body.courseName, dept, body.status, prereq, body.creditHour)
                : new Courses(body.courseId, body.courseName, dept, body.status, body.creditHour);

        coursesRepository.save(course);

        return new CourseResponse(
                course.getCourseId(),
                course.getCourseName(),
                course.getStatus(),
                course.getCreditHour(),
                new CourseResponse.DepartmentInfo(dept.getDeptId(), dept.getDeptName()),
                prereq != null ? new CourseResponse.PrerequisiteCourseInfo(prereq.getCourseId(), prereq.getCourseName()) : null
        );
    }
}

