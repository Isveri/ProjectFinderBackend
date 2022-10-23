package com.example.project.samples;

import com.example.project.domain.Report;
import com.example.project.model.ReportDTO;

import static com.example.project.samples.UserMockSample.*;

public class ReportMockSample {

    public static Report getReportMock(){
        return Report.builder()
                .id(1L)
                .date("26-09-2022")
                .reason("Toxicity")
                .reportedBy(getCurrentUserMock())
                .reportedUser(getUserMock())
                .build();
    }
    public static ReportDTO getReportDTOMock() {
        return ReportDTO.builder()
                .id(1L)
                .date("26-09-2022")
                .reason("Toxicity")
                .reportedBy(getUserMsgDTOMock())
                .build();
    }
    public static ReportDTO getReportDTOMockv2(){
        return ReportDTO.builder()
                .id(1L)
                .date("26-09-2022")
                .reason("Toxicity")
                .reportedBy(getUserMsgDTOMockv2())
                .build();
    }
}
