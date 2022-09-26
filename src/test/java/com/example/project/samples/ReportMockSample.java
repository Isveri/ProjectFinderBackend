package com.example.project.samples;

import com.example.project.model.ReportDTO;


import static com.example.project.samples.UserMockSample.getUserMsgDTOMock;

public class ReportMockSample {

    public static ReportDTO getReportDTOMock() {
        return ReportDTO.builder()
                .id(1L)
                .date("26-09-2022")
                .reason("Toxicity")
                .reportedBy(getUserMsgDTOMock())
                .build();
    }
}
