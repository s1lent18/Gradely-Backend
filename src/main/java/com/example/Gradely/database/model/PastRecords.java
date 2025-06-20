package com.example.Gradely.database.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "pastRecords")
public class PastRecords {

    @Id
    private String id;

    private String courseId;

    private List<Records> records;

    @Data
    public static class Records {
        private String sectionId;
        private String teacherId;
        private Double avgScore;
        private String avgGrade;
        private Double highest;
        private Double lowest;
        private Double rating;
        private String bestComment;
        private String worstComment;

        public Records() {}

        public Records(String sectionId, String teacherId, Double avgScore, String avgGrade, Double highest, Double lowest, Double rating, String bestComment, String worstComment) {
            this.sectionId = sectionId;
            this.teacherId = teacherId;
            this.avgScore = avgScore;
            this.avgGrade = avgGrade;
            this.highest = highest;
            this.lowest = lowest;
            this.rating = rating;
            this.bestComment = bestComment;
            this.worstComment = worstComment;
        }
    }
}