package com.example.Gradely.service;

import com.example.Gradely.database.model.Courses;
import com.example.Gradely.database.model.Departments;
import com.example.Gradely.database.model.Teachers;
import com.example.Gradely.database.model.Teachers;
import com.example.Gradely.database.repository.DepartmentsRepository;
import com.example.Gradely.database.repository.TeachersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeachersService {

    private final TeachersRepository teachersRepository;
    private final DepartmentsRepository departmentsRepository;

    public TeachersService(TeachersRepository teachersRepository, DepartmentsRepository departmentsRepository) {
        this.teachersRepository = teachersRepository;
        this.departmentsRepository = departmentsRepository;
    }

    public static class TeacherRequest {
        public String teacherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String gender;
        public String qualification;
        public Long departmentId;
    }

    public static class TeachersResponse {
        public String teacherId;
        public String teacherName;
        public String bloodGroup;
        public String address;
        public String personalEmail;
        public String phone;
        public String emergency;
        public String qualification;
        public String gender;
        public String password;
        public String assignedEmail;
        public TeachersService.TeachersResponse.DepartmentInfo department;

        public TeachersResponse(String teacherId, String teacherName, String bloodGroup, String address, String personalEmail, String phone, String emergency, String qualification, String gender, String password, String assignedEmail, TeachersService.TeachersResponse.DepartmentInfo department) {
            this.teacherId = teacherId;
            this.teacherName = teacherName;
            this.bloodGroup = bloodGroup;
            this.address = address;
            this.personalEmail = personalEmail;
            this.phone = phone;
            this.emergency = emergency;
            this.qualification = qualification;
            this.gender = gender;
            this.password = password;
            this.assignedEmail = assignedEmail;
            this.department = department;
        }

        public static class DepartmentInfo {
            public Long deptId;
            public String deptName;

            public DepartmentInfo(Long deptId, String deptName) {
                this.deptId = deptId;
                this.deptName = deptName;
            }
        }
    }

    @Transactional
    public TeachersService.TeachersResponse add(TeachersService.TeacherRequest body) {
        Departments dept = departmentsRepository.findById(body.departmentId).orElseThrow(() -> new RuntimeException("Department not found"));

        Teachers teacher = new Teachers(body.teacherName, body.bloodGroup, body.address, "", body.personalEmail, body.phone, body.emergency, body.qualification, "Probation", dept, body.gender);

        Teachers savedTeacher = teachersRepository.save(teacher);

        String assignedEmail = body.teacherName.replaceAll("\\s+", ".").toLowerCase() + "." + savedTeacher.getTeacherId() + "@unifaculty.com";

        savedTeacher.setAssignedEmail(assignedEmail);

        teachersRepository.save(savedTeacher);

        return new TeachersService.TeachersResponse(
            String.valueOf(savedTeacher.getTeacherId()),
            savedTeacher.getTeacherName(),
            savedTeacher.getBloodGroup(),
            savedTeacher.getAddress(),
            savedTeacher.getPersonalEmail(),
            savedTeacher.getPhone(),
            savedTeacher.getEmergency(),
            savedTeacher.getQualification(),
            savedTeacher.getGender(),
            savedTeacher.getPassword(),
            savedTeacher.getAssignedEmail(),
            new TeachersResponse.DepartmentInfo(dept.getDeptId(), dept.getDeptName())
        );
    }
}
