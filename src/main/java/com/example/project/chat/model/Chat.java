package com.example.project.chat.model;

import com.example.project.chat.model.Message;
import com.example.project.domain.GroupRoom;
import lombok.*;

import javax.persistence.*;
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
