package com.example.project.model.auth;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDTO {
    public String oldPassword;
    public String newPassword;
}
