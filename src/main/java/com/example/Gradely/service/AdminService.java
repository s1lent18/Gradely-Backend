package com.example.Gradely.service;

import com.example.Gradely.database.model.Course;
import com.example.Gradely.database.model.Registrations;
import com.example.Gradely.database.model.Section;
import com.example.Gradely.database.repository.CoursesRepository;
import com.example.Gradely.database.repository.RegistrationsRepository;
import com.example.Gradely.database.repository.SectionRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final RegistrationsRepository registrationsRepository;
    private final CoursesRepository coursesRepository;
    private final SectionRepository sectionRepository;

    public AdminService(
            CoursesRepository coursesRepository,
            SectionRepository sectionRepository,
            RegistrationsRepository registrationsRepository
    ) {
        this.coursesRepository = coursesRepository;
        this.sectionRepository = sectionRepository;
        this.registrationsRepository = registrationsRepository;
    }

    @Getter
    public static class AdminLoginRequest {
        public String adminEmail;
        public String adminPassword;
    }

    public static class CourseRegistrationInit {
        public String courseId;
        public String courseCode;
        public String courseName;
        public String status;
        public String desc;
        public List<String> teachers;
        public List<String> sections;
    }

    public static class CourseRegistrationAdd {
        public String courseId;
        public String desc;
    }

    @Transactional
    public String addCoursesForRegistration(List<CourseRegistrationAdd> request) {
        List<String> courseIds = request.stream().map(r -> r.courseId).toList();

        Map<String, String> courseDescMap = request.stream()
                .collect(Collectors.toMap(r -> r.courseId, r -> r.desc));

        List<Course> courses = coursesRepository.findAllById(courseIds);
        if (courses.size() != courseIds.size()) {
            throw new RuntimeException("Some course IDs are invalid");
        }

        List<Section> allSections = sectionRepository.findAll();

        Map<String, List<Section>> sectionByCourse = new HashMap<>();

        for (Section section : allSections) {
            if (section.getClasses() == null) continue;

            for (Section.Class cls : section.getClasses()) {
                if (cls.getCourse() != null && courseIds.contains(cls.getCourse())) {
                    sectionByCourse
                            .computeIfAbsent(cls.getCourse(), k -> new ArrayList<>())
                            .add(section);
                    break;
                }
            }
        }

        List<CourseRegistrationInit> courseOptions = new ArrayList<>();

        for (Course course : courses) {
            List<Section> courseSections = sectionByCourse.getOrDefault(course.getId(), Collections.emptyList());

            Set<String> sectionIds = new HashSet<>();
            Set<String> teacherIds = new HashSet<>();

            for (Section section : courseSections) {
                sectionIds.add(section.getId());

                for (Section.Class cls : section.getClasses()) {
                    if (cls.getCourse().equals(course.getId()) && cls.getTeacher() != null) {
                        teacherIds.add(cls.getTeacher());
                    }
                }
            }

            if (sectionIds.isEmpty() || teacherIds.isEmpty()) {
                throw new RuntimeException("Course [" + course.getCourseCode() + "] is not associated with any section or teacher.");
            }

            CourseRegistrationInit dto = new CourseRegistrationInit();
            dto.courseId = course.getId();
            dto.courseCode = course.getCourseCode();
            dto.courseName = course.getCourseName();
            dto.status = course.getStatus();
            dto.desc = courseDescMap.get(course.getId());
            dto.sections = new ArrayList<>(sectionIds);
            dto.teachers = new ArrayList<>(teacherIds);

            courseOptions.add(dto);
        }

        Registrations registration = new Registrations();
        registration.setAvailableCourses(courseOptions);
        Registrations saved = registrationsRepository.save(registration);

        return saved.getId();
    }

    @Transactional
    public List<CourseRegistrationInit> allowCourseRegistration() {

        return registrationsRepository.findFirstByOrderByCreatedAtDesc()
                .map(Registrations::getAvailableCourses)
                .orElseThrow(() -> new RuntimeException("No registration options available"));
    }

    @Transactional
    public void clearTeacherAndSectionsFromCourse(String courseId) {
        Course course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTeachers(new ArrayList<>());

        coursesRepository.save(course);
    }
}