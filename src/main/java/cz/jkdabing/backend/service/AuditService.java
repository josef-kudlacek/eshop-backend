package cz.jkdabing.backend.service;

import cz.jkdabing.backend.entity.UserEntity;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface AuditService {

    void prepareAuditLog(String entityName, UUID entityId, String action);

    void createAuditLog(ZonedDateTime loggedAt, UserEntity user, String entityName, UUID entityId, String action);
}
