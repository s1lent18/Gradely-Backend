package com.example.Gradely.database.model;

import java.security.SecureRandom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.Year;
import java.util.List;

@Data
@Document(collection = "teachers")
public class Teacher {

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#%&*!";

    private static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }

    @Id
    private String id;

    private String name;
    private String personalEmail;
    private String assignedEmail;
    private String bloodGroup;
    private String address;
    private String phone;
    private String emergency;
    private String hiringYear;
    private String position;
    private List<String> qualification;
    private String status;
    private String gender;
    private int departmentId;
    private int age;
    private String password;
    private List<CourseInfo> courses;

    @Data
    public static class CourseInfo {
        private String id;
        private int rating;
        private String worstRatedComment;
        private String bestRatedComment;

        public String getId() {
            return id;
        }

        public int getRating() {
            return rating;
        }

        public String getWorstRatedComment() {
            return worstRatedComment;
        }

        public String getBestRatedComment() {
            return bestRatedComment;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public void setBestRatedComment(String bestRatedComment) {
            this.bestRatedComment = bestRatedComment;
        }

        public void setWorstRatedComment(String worstRatedComment) {
            this.worstRatedComment = worstRatedComment;
        }

        public CourseInfo() {}

        public CourseInfo(String id, int rating, String worstRatedComment, String bestRatedComment) {
            this.id = id;
            this.rating = rating;
            this.worstRatedComment = worstRatedComment;
            this.bestRatedComment = bestRatedComment;
        }
    }

    public Teacher() {}

    public Teacher(String name, String personalEmail, String bloodGroup, String address, String phone, String emergency, String position, List<String> qualification, String gender, Integer age) {
        this.name = name;
        this.personalEmail = personalEmail;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.phone = phone;
        this.emergency = emergency;
        this.position = position;
        this.hiringYear = String.valueOf(Year.now().getValue());
        this.qualification = qualification;
        this.gender = gender;
        this.age = age;
        this.password = generateRandomPassword();
        this.status = "Probation";
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public static int getPasswordLength() {
        return PASSWORD_LENGTH;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getAssignedEmail() {
        return assignedEmail;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public static String getCharSet() {
        return CHAR_SET;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setAssignedEmail(String assignedEmail) {
        this.assignedEmail = assignedEmail;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getEmergency() {
        return emergency;
    }

    public String getId() {
        return id;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHiringYear() {
        return hiringYear;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setQualification(List<String> qualification) {
        this.qualification = qualification;
    }

    public void setCourses(List<CourseInfo> courses) {
        this.courses = courses;
    }

    public void setHiringYear(String hiringYear) {
        this.hiringYear = hiringYear;
    }

    public int getAge() {
        return age;
    }

    public List<String> getQualification() {
        return qualification;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public List<CourseInfo> getCourses() {
        return courses;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}