package com.example.project.model;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;
    private String reason;
    private UserMsgDTO reportedBy;
}
