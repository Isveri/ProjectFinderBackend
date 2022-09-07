package com.example.project.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sendingUser_id")
    private User sendingUser;

    @OneToOne
    @JoinColumn(name="invitedUser_id")
    private User invitedUser;

    @Builder.Default
    private boolean accepted = false;
}
