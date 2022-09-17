package com.example.project.security.emailConfirm;


import com.example.project.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnEmailChangeCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private User user;

    public OnEmailChangeCompleteEvent(User user,Locale locale , String appUrl){
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
