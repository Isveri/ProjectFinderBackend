package com.example.project.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCriteria {

    private Long gameId;
    private Long categoryId;
    private Long roleId;
    private String cityName;
}
