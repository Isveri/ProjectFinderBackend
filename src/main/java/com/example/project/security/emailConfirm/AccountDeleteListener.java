package com.example.project.security.emailConfirm;

import com.example.project.domain.User;
import com.example.project.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;
@RequiredArgsConstructor
@Component
public class AccountDeleteListener implements ApplicationListener<OnAccountDeleteCompleteEvent> {
    private final AuthService authService;

    private final JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnAccountDeleteCompleteEvent event) {
        this.confirmAccountDelete(event);

    }

    private void confirmAccountDelete(OnAccountDeleteCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        authService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Account Delete Confirmation";


        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Your verification token: "+token);
        javaMailSender.send(email);
    }

}
