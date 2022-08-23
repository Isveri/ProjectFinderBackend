package com.example.project.domain;

import com.example.project.chat.model.Chat;
import lombok.*;
import org.hibernate.annotations.Where;

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
@Where(clause = "deleted=false")
//TODO w momencie kiedy bedziemy tworzyli panel admina mozliwe ze bedzie trzeba zmienic @Where bo nie bedziemy w stanie wyciagnac
//TODO usunietych grup itd. Mozliwe tez ze bedzie mozna to obejsc za pomoca Query przy metodzie i uzyciu nativeQuery=true
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

    @Builder.Default
    private boolean open = true;

    @ManyToMany(mappedBy = "groupRooms",cascade = CascadeType.MERGE)
    private List<User> users = new ArrayList<>();

    @OneToOne(mappedBy = "groupRoom",cascade = CascadeType.REMOVE)
    private Chat chat;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="groupLeader_id")
    private User groupLeader;

    private String joinCode;

    private String city;

    private boolean deleted = false;
}
