package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.AuthorMapper;
import cz.jkdabing.backend.repository.AuthorRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.AuthorService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthorServiceImpl extends AbstractService implements AuthorService {

    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(
            MessageService messageService,
            AuthorMapper authorMapper,
            AuthorRepository authorRepository,
            AuditService auditService) {
        super(messageService, auditService);
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDTO> getAuthors() {
        return authorRepository.findAll().stream()
                .sorted(getAuthorEntityComparator())
                .map(authorMapper::toDTO)
                .toList();
    }

    @Override
    public void createAuthor(@Valid AuthorDTO authorDTO) {
        AuthorEntity authorEntity = authorMapper.toEntity(authorDTO);
        authorRepository.save(authorEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(authorEntity.getClass()),
                authorEntity.getAuthorId(),
                AuditLogConstants.ACTION_CREATE
        );
    }

    @Override
    public void updateAuthor(@NotEmpty String authorId, @Valid AuthorDTO authorDTO) {
        authorDTO.setAuthorId(UUID.fromString(authorId));
        AuthorEntity authorEntity = findAuthorByIdOrThrow(UUID.fromString(authorId));
        authorEntity = authorMapper.updateFromDTO(authorDTO, authorEntity);

        authorRepository.save(authorEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(authorEntity.getClass()),
                authorEntity.getAuthorId(),
                AuditLogConstants.ACTION_UPDATE
        );
    }

    @Override
    public void deleteAuthor(@NotEmpty String authorId) {
        UUID internalId = UUID.fromString(authorId);

        checkAuthorExistsByIdOrThrow(internalId);
        authorRepository.deleteById(internalId);

        prepareAuditLog(
                TableNameUtil.getTableName(AuthorEntity.class),
                internalId,
                AuditLogConstants.ACTION_DELETE
        );
    }

    @Override
    public void checkAuthorExistsByIdOrThrow(@NotNull UUID authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new NotFoundException(getLocalizedMessage("error.author.not.found", authorId)
            );
        }
    }

    @Override
    public AuthorEntity findAuthorByIdOrThrow(@NotNull UUID authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.author.not.found", authorId))
                );
    }

    private Comparator<AuthorEntity> getAuthorEntityComparator() {
        Collator collator = Collator.getInstance(Locale.forLanguageTag("cs-CZ"));

        return (authorEntityOne, authorEntityTwo) -> {
            int lastNameComparison = collator.compare(authorEntityOne.getLastName(), authorEntityTwo.getLastName());
            if (lastNameComparison != 0) {
                return lastNameComparison;
            } else {
                return collator.compare(authorEntityTwo.getFirstName(), authorEntityOne.getFirstName());
            }
        };
    }
}
