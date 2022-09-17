package com.example.project.security.emailConfirm;

import com.example.project.domain.User;
import com.example.project.services.AuthService;
import com.example.project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;
@RequiredArgsConstructor
@Component
public class EmailChangeListener implements ApplicationListener<OnEmailChangeCompleteEvent> {
    private final AuthService authService;
    private final MessageSource messageSource;

    private final JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnEmailChangeCompleteEvent event) {
        this.confirmEmailChange(event);
    }

    private void confirmEmailChange(OnEmailChangeCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        authService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/emailChangeConfirm?token=" + token;
        String message = messageSource.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        javaMailSender.send(email);
    }
}
