package com.example.project.mappers;

import com.example.project.domain.Report;
import com.example.project.model.ReportDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.ReportMockSample.getReportDTOMock;
import static com.example.project.samples.ReportMockSample.getReportMock;
import static org.junit.jupiter.api.Assertions.*;

class ReportMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final ReportMapper reportMapper = new ReportMapperImpl(userMapper);

    @Test
    void should_map_report_to_reportDTO() {
        //given
        Report report = getReportMock();

        //when
        ReportDTO result = reportMapper.mapReportToReportDTO(report);

        //then
        assertEquals(report.getId(),result.getId());
        assertEquals(report.getReportedBy().getId(), result.getReportedBy().getId());
        assertEquals(report.getDate(),result.getDate());
        assertEquals(report.getReason(),result.getReason());

    }

    @Test
    void should_map_reportDTO_to_report() {
        //given
        ReportDTO reportDTO = getReportDTOMock();

        //when
        Report result = reportMapper.mapReportDTOToReport(reportDTO);

        //then
        assertEquals(reportDTO.getId(),result.getId());
        assertEquals(reportDTO.getReportedBy().getId(), result.getReportedBy().getId());
        assertEquals(reportDTO.getDate(),result.getDate());
        assertEquals(reportDTO.getReason(),result.getReason());
    }
}