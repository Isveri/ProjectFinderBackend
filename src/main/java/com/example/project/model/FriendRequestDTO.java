package com.example.project.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class FriendRequestDTO {

    private Long id;
    private UserMsgDTO sendingUser;
    private UserMsgDTO invitedUser;
    private boolean accepted;
}
