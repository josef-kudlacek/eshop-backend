package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
}
