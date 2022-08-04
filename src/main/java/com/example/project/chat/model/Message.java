package com.example.project.chat.model;

import com.example.project.domain.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @ManyToOne()
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="chat_id")
    private Chat chat;

    private LocalDateTime time;

}
