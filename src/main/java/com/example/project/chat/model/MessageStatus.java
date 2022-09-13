package com.example.project.chat.model;

import com.example.project.domain.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class MessageStatus {

    public enum Status{
        READ,
        UNREAD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Status status = Status.UNREAD;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(cascade=CascadeType.MERGE)
    private Message message;
}
