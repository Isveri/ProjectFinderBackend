package com.example.project.security.emailConfirm;

import com.example.project.domain.User;
import com.example.project.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AccountRegisterListener implements ApplicationListener<OnAccountRegisterCompleteEvent> {
    private final AuthService authService;

    private final JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnAccountRegisterCompleteEvent event) {
        this.confirmAccountRegister(event);
    }
    private void confirmAccountRegister(OnAccountRegisterCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        authService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Account Register Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/confirmRegister?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("To enable your account go under link below: http://localhost:4200/#" + confirmationUrl);
        javaMailSender.send(email);
    }

}
