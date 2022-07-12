package com.example.project.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @OneToMany(mappedBy = "groupRoom",cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "groupRooms",cascade = CascadeType.MERGE)
    private List<User> users = new ArrayList<>();


}
