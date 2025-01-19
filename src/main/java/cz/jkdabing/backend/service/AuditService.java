package cz.jkdabing.backend.service;

import cz.jkdabing.backend.entity.UserEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditService {

    void prepareAuditLog(String entityName, UUID entityId, String action);

    void prepareAuditLogs(String entityName, List<UUID> entityId, String action);

    void createAuditLog(ZonedDateTime loggedAt, UserEntity user, String entityName, UUID entityId, String action);

    void createAuditLogs(ZonedDateTime loggedAt, UserEntity user, String entityName, List<UUID> entityId, String action);
}
