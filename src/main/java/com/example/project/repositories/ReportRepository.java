package com.example.project.repositories;

import com.example.project.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

     boolean existsByReportedByIdAndReportedUserId(Long reportedById, Long reportedUserId);

}
