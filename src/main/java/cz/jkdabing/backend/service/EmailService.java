package cz.jkdabing.backend.service;

public interface EmailService {

    void sendActivationEmail(String recipient, String activationLink);
}
