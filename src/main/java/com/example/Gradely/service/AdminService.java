package com.example.Gradely.service;

import com.example.Gradely.database.model.*;
import com.example.Gradely.database.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final RegistrationsRepository registrationsRepository;
    private final CoursesRepository coursesRepository;
    private final SectionRepository sectionRepository;
    private final TeachersRepository teachersRepository;
    private final PastRecordsRepository pastRecordsRepository;
    private final StudentsRepository studentsRepository;

    public AdminService(
            CoursesRepository coursesRepository,
            SectionRepository sectionRepository,
            RegistrationsRepository registrationsRepository,
            TeachersRepository teachersRepository,
            PastRecordsRepository pastRecordsRepository,
            StudentsRepository studentsRepository
    ) {
        this.coursesRepository = coursesRepository;
        this.sectionRepository = sectionRepository;
        this.registrationsRepository = registrationsRepository;
        this.teachersRepository = teachersRepository;
        this.pastRecordsRepository = pastRecordsRepository;
        this.studentsRepository = studentsRepository;
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

    @Setter
    @Getter
    public static class CourseStats {
        private Double avgScore;
        private Double highest;
        private Double lowest;
        private String avgGrade;

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
    public void clearTeacherAndSectionsFromCourse() {
        List<Course> courses = coursesRepository.findAll();
        List<Section> sections = sectionRepository.findAll();
        List<Teacher> teachers = teachersRepository.findAll();

        List<PastRecords> pastRecords = new ArrayList<>();

        for (Course course : courses) {
//            CourseStats stats = calculateStatsForCourse(course.getId());
//
//            List<PastRecords.Records> recordsList = getRecords(course, teachers, stats);
//
//            if (!recordsList.isEmpty()) {
//                PastRecords past = new PastRecords();
//                past.setCourseId(course.getId());
//                past.setRecords(recordsList);
//                pastRecords.add(past);
//            }

            course.setTeachers(new ArrayList<>());
        }

        for (Section section : sections) {
            section.setClasses(new ArrayList<>());
        }

        for (Teacher teacher : teachers) {
            teacher.setSections(new ArrayList<>());
        }

        pastRecordsRepository.saveAll(pastRecords);
        coursesRepository.saveAll(courses);
        sectionRepository.saveAll(sections);
        teachersRepository.saveAll(teachers);
    }

    private List<PastRecords.Records> getRecords(Course course, List<Teacher> teachers, CourseStats stats) {
        List<PastRecords.Records> recordsList = new ArrayList<>();

        for (Teacher teacher : teachers) {
            for (Teacher.Section section : teacher.getSections()) {
                for (Teacher.CourseInfo c : section.getCourse()) {
                    if (c.getId().equals(course.getId())) {
                        PastRecords.Records record = new PastRecords.Records(
                                section.getName(),
                                teacher.getId(),
                                stats != null ? stats.getAvgScore() : null,
                                stats != null ? stats.getAvgGrade() : null,
                                stats != null ? stats.getHighest() : null,
                                stats != null ? stats.getLowest() : null,
                                c.getRating(),
                                c.getBestRatedComment(),
                                c.getWorstRatedComment()
                        );
                        recordsList.add(record);
                    }
                }
            }
        }

        return recordsList;
    }

    public CourseStats calculateStatsForCourse(String courseId) {
        List<Student> students = studentsRepository.findByCourseId(courseId);

        List<Double> totalScores = new ArrayList<>();

        for (Student student : students) {
            if (student.getSemesters() == null) continue;

            for (Student.Semester semester : student.getSemesters()) {
                if (semester.getCourses() == null) continue;

                for (Student.Courses course : semester.getCourses()) {
                    if (course.getCourseId().equals(courseId)) {
                        Student.Course details = course.getDetails();
                        if (details != null) {
                            double score = 0;

                            score += sumList(details.getMid1());
                            score += sumList(details.getMid2());
                            score += sumList(details.getFinalExam());
                            score += sumList(details.getClassParticipation());
                            score += sumNestedList(details.getAssignments());
                            score += sumNestedList(details.getQuizzes());
                            score += sumList(details.getProject());

                            totalScores.add(score);
                        }
                    }
                }
            }
        }

        if (totalScores.isEmpty()) return null;

        double avg = totalScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double high = Collections.max(totalScores);
        double low = Collections.min(totalScores);
        String grade = calculateGrade(avg);

        CourseStats stats = new CourseStats();
        stats.setAvgScore(avg);
        stats.setHighest(high);
        stats.setLowest(low);
        stats.setAvgGrade(grade);

        return stats;
    }

    private double sumList(List<String> list) {
        if (list == null) return 0;
        return list.stream().mapToDouble(s -> {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        }).sum();
    }

    private double sumNestedList(List<List<String>> nestedList) {
        if (nestedList == null) return 0;
        return nestedList.stream().mapToDouble(this::sumList).sum();
    }

    private String calculateGrade(double score) {
        if (score >= 90) return "A+";
        if (score >= 86) return "A";
        if (score >= 82) return "A-";
        if (score >= 78) return "B+";
        if (score >= 74) return "B";
        if (score >= 70) return "B-";
        if (score >= 66) return "C+";
        if (score >= 62) return "C";
        if (score >= 58) return "C-";
        if (score >= 54) return "D+";
        if (score >= 50) return "D";
        return "F";
    }
}