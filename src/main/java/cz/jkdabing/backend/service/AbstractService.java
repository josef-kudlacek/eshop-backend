package cz.jkdabing.backend.service;

import java.util.UUID;

public abstract class AbstractService {

    private final MessageService messageService;

    private final AuditService auditService;

    protected AbstractService(MessageService messageService, AuditService auditService) {
        this.messageService = messageService;
        this.auditService = auditService;
    }

    protected String getLocalizedMessage(String key, Object... args) {
        return messageService.getMessage(key, args);
    }

    protected String getLocalizedMessage(String key) {
        return messageService.getMessage(key);
    }

    protected void prepareAuditLog(String entityName, UUID entityId, String action) {
        auditService.prepareAuditLog(entityName, entityId, action);
    }
}
