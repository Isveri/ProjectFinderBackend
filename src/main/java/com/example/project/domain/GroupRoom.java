package com.example.project.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class GroupRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @JoinColumn(name = "category_id")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Category category;

    @JoinColumn(name = "game_id")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Game game;

    @NotNull
    private int maxUsers;

    /**
     * 1 na liscie to leader pog
     */
    @ManyToMany(mappedBy = "groupRooms",cascade = CascadeType.MERGE)
    private List<User> users = new ArrayList<>();

    @OneToOne(mappedBy = "groupRoom",cascade = CascadeType.REMOVE)
    private Chat chat;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="groupLeader_id")
    private User groupLeader;

//    @NotBlank
    private Long joinCode;
}
