package cz.jkdabing.backend.service;


public interface MessageService {

    String getMessage(String key, Object... args);

    String getMessage(String key);
}
