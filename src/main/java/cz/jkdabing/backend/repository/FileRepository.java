package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    boolean existsByFileName(String fileName);
}
