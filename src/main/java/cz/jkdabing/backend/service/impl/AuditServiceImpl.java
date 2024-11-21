package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.entity.AuditLogEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.repository.AuditLogRepository;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.SecurityService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuditServiceImpl implements AuditService {

    @Resource
    @Lazy
    private AuditService auditService;

    private final AuditLogRepository auditLogRepository;

    private final SecurityService service;

    public AuditServiceImpl(AuditLogRepository auditLogRepository, SecurityService service) {
        this.auditLogRepository = auditLogRepository;
        this.service = service;
    }

    @Override
    public void prepareAuditLog(String entityName, UUID entityId, String action) {
        UserEntity currentUser = service.getCurrentUser();

        auditService.createAuditLog(ZonedDateTime.now(), currentUser, entityName, entityId, action);
    }

    @Override
    @Async
    @Transactional
    public void createAuditLog(ZonedDateTime loggedAt, UserEntity user, String entityName, UUID entityId, String action) {
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoggedAt(loggedAt);
        auditLogEntity.setUser(user);
        auditLogEntity.setTableName(entityName);
        auditLogEntity.setEntityId(entityId);
        auditLogEntity.setAction(action);

        auditLogRepository.save(auditLogEntity);
    }
}
