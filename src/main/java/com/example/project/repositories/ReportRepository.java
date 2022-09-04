package com.example.project.repositories;

import com.example.project.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

     List<Report> findAllByReportedUserEnabled(boolean value);
     boolean existsByReportedByIdAndReportedUserId(Long reportedById, Long reportedUserId);

}
