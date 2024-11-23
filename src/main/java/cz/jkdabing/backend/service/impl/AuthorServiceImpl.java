package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import cz.jkdabing.backend.exception.NotFoundException;
import cz.jkdabing.backend.mapper.AuthorMapper;
import cz.jkdabing.backend.repository.AuthorRepository;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.AuthorService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;

    private final AuditService auditService;

    public AuthorServiceImpl(AuthorMapper authorMapper, AuthorRepository authorRepository, AuditService auditService) {
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
        this.auditService = auditService;
    }

    @Override
    public void createAuthor(AuthorDTO authorDTO) {
        AuthorEntity authorEntity = authorMapper.toEntity(authorDTO);
        authorRepository.save(authorEntity);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(authorEntity.getClass()),
                authorEntity.getAuthorId(),
                AuditLogConstants.ACTION_CREATE
        );
    }

    @Override
    public void updateAuthor(String authorId, AuthorDTO authorDTO) {
        authorDTO.setAuthorId(UUID.fromString(authorId));
        AuthorEntity authorEntity = findAuthorByIdOrThrow(UUID.fromString(authorId));
        authorEntity = authorMapper.updateFromDTO(authorDTO, authorEntity);

        authorRepository.save(authorEntity);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(authorEntity.getClass()),
                authorEntity.getAuthorId(),
                AuditLogConstants.ACTION_UPDATE
        );
    }

    @Override
    public void deleteAuthor(String authorId) {
        UUID internalId = UUID.fromString(authorId);

        checkAuthorExistsByIdOrThrow(internalId);
        authorRepository.deleteById(internalId);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(AuthorEntity.class),
                internalId,
                AuditLogConstants.ACTION_DELETE
        );
    }

    @Override
    public void checkAuthorExistsByIdOrThrow(UUID authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new NotFoundException("Author not found, id was: " + authorId);
        }
    }

    @Override
    public AuthorEntity findAuthorByIdOrThrow(UUID authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found, id was: " + authorId));
    }
}
