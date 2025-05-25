package com.example.Gradely.database.repository;

import com.example.Gradely.database.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {

    Optional<Students> findByAssignedEmail(String assignedEmail);

    List<Students> findByStudentName(String studentName);

    List<Students> findByBatch(String batch);

    List<Students> findByDegree(String degree);

    Optional<Students> findByPhone(String phone);

    long countByBatch(String batch);

    List<Students> findByCpgaBetween(Float minCpga, Float maxCpga);

    @Query("SELECT COUNT(s) FROM Students s WHERE s.degree = :degree AND s.batch = :batch AND s.section = :section")
    long countByDegreeAndBatchAndSection(@Param("degree") String degree, @Param("batch") String batch, @Param("section") String section);
}
