package com.example.project.mappers;

import com.example.project.domain.Report;
import com.example.project.model.ReportDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ReportMapper {

    public abstract ReportDTO mapReportToReportDTO(Report report);

    public abstract Report mapReportDTOToReport(ReportDTO reportDTO);

}
