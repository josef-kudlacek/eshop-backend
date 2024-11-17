package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.EmailConstants;
import cz.jkdabing.backend.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendActivationEmail(String recipient, String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(EmailConstants.ACTIVATION_MAIL_SUBJECT);
        message.setText(prepareActivationText(activationLink));

        mailSender.send(message);
    }

    private String prepareActivationText(String activationLink) {
        return "Please click on the following link to activate your account: " + activationLink;
    }
}
