package com.example.project.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "game",cascade = CascadeType.MERGE)
    private List<InGameRole> inGameRoles;

    @OneToMany(mappedBy = "game", cascade = CascadeType.MERGE)
    private List<Category> categories;

    @OneToMany(mappedBy = "game",cascade =  CascadeType.MERGE)
    private List<GroupRoom> groupRooms;

    @Builder.Default
    private boolean assignRolesActive = true;


}
