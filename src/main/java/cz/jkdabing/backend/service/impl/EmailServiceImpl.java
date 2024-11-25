package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.service.EmailService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final MessageService messageService;

    public EmailServiceImpl(
            MessageService messageService,
            JavaMailSender mailSender
    ) {
        this.messageService = messageService;
        this.mailSender = mailSender;
    }

    @Override
    public void sendActivationEmail(@NotEmpty String recipient, @NotEmpty String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(messageService.getMessage("activation.email.subject"));
        message.setText(prepareActivationText(activationLink));

        mailSender.send(message);
    }

    private String prepareActivationText(@NotEmpty String activationLink) {
        return messageService.getMessage("activation.email.text", activationLink);
    }
}
