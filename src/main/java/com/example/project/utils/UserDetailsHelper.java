package com.example.project.utils;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetailsHelper {

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean checkPrivilages(GroupRoom groupRoom){
        User user = getCurrentUser();
        return groupRoom.getGroupLeader().getId().equals(user.getId()) || user.getRole().getName().equals("ROLE_ADMIN");
    }
}
