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
import java.util.List;
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
    public void prepareAuditLogs(String entityName, List<UUID> entityIds, String action) {
        UserEntity currentUser = service.getCurrentUser();

        auditService.createAuditLogs(ZonedDateTime.now(), currentUser, entityName, entityIds, action);
    }

    @Override
    @Async
    @Transactional
    public void createAuditLog(
            @NotNull ZonedDateTime loggedAt,
            UserEntity user,
            @NotEmpty String entityName,
            @NotNull UUID entityId,
            @NotEmpty String action
    ) {
        AuditLogEntity auditLogEntity = AuditLogEntity.builder()
                .loggedAt(loggedAt)
                .user(user)
                .tableName(entityName)
                .entityId(entityId)
                .action(action)
                .build();

        auditLogRepository.save(auditLogEntity);
    }

    @Override
    @Async
    @Transactional
    public void createAuditLogs(ZonedDateTime loggedAt, UserEntity user, String entityName, List<UUID> entityIds, String action) {
        List<AuditLogEntity> auditLogEntities = entityIds.stream()
                .map(entityId -> AuditLogEntity.builder()
                        .loggedAt(loggedAt)
                        .user(user)
                        .tableName(entityName)
                        .entityId(entityId)
                        .action(action)
                        .build())
                .toList();

        auditLogRepository.saveAll(auditLogEntities);
    }
}
