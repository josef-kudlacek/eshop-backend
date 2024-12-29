package cz.jkdabing.backend.repository;

import cz.jkdabing.backend.entity.AudioFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AudioFileRepository extends JpaRepository<AudioFileEntity, UUID> {

    boolean existsByFileName(String fileName);
}
