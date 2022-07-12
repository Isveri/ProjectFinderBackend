package com.example.project.domain;

import com.sun.istack.NotNull;
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
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade=CascadeType.MERGE)
    private GroupRoom groupRoom;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.MERGE)
    private List<Message> messages;
}
