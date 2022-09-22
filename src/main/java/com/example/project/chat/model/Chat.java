package com.example.project.chat.model;

import com.example.project.chat.model.Message;
import com.example.project.domain.Friend;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
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

    @OneToOne(cascade=CascadeType.REMOVE)
    private GroupRoom groupRoom;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.MERGE)
    private List<Message> messages;

    @OneToMany(mappedBy ="chat",cascade = CascadeType.MERGE)
    private List<Friend> users;

    @Builder.Default
    private boolean notPrivate = true;
}
