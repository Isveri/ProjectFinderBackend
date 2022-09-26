package com.example.project.model;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;
    private String reason;
    private UserMsgDTO reportedBy;
    private String date;
}
