package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.service.MessageService;

public abstract class AbstractBaseController {

    private final MessageService messageService;

    protected AbstractBaseController(MessageService messageService) {
        this.messageService = messageService;
    }

    protected String getLocalizedMessage(String key, Object... args) {
        return messageService.getMessage(key, args);
    }

    protected String getLocalizedMessage(String key) {
        return messageService.getMessage(key);
    }
}
