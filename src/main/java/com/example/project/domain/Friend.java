package com.example.project.domain;

import com.example.project.chat.model.Chat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Builder.Default
    private boolean online = false;

    @ManyToOne()
    @JoinColumn(name="chat_id")
    private Chat chat;
}
