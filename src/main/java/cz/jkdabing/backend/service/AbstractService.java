package cz.jkdabing.backend.service;

import java.util.List;
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

    protected void prepareAuditLog(String entityName, Long entityId, String action) {
        String uuid = String.format("00000000-0000-0000-0000-%012d", entityId);
        auditService.prepareAuditLog(entityName, UUID.fromString(uuid), action);
    }

    protected void prepareAuditLogs(String entityName, List<UUID> entityIds, String action) {
        auditService.prepareAuditLogs(entityName, entityIds, action);
    }
}
