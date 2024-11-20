package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.NotFoundException;
import cz.jkdabing.backend.mapper.AuthorMapper;
import cz.jkdabing.backend.repository.AuthorRepository;
import cz.jkdabing.backend.service.AuthorService;
import cz.jkdabing.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final UserService userService;

    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(UserService userService, AuthorMapper authorMapper, AuthorRepository authorRepository) {
        this.userService = userService;
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
    }

    @Override
    public void createAuthor(AuthorDTO authorDTO) {
        AuthorEntity authorEntity = authorMapper.toEntity(authorDTO);

        UserEntity currentUser = userService.getCurrentUser();
        authorEntity.setCreatedBy(currentUser);

        authorRepository.save(authorEntity);
    }

    @Override
    public void updateAuthor(String authorId, AuthorDTO authorDTO) {
        authorDTO.setAuthorId(UUID.fromString(authorId));
        AuthorEntity authorEntity = findAuthorByIdOrThrow(UUID.fromString(authorId));
        authorEntity = authorMapper.updateFromDTO(authorDTO, authorEntity);

        UserEntity currentUser = userService.getCurrentUser();
        authorEntity.setUpdatedBy(currentUser);

        authorRepository.save(authorEntity);
    }

    @Override
    public void deleteAuthor(String authorId) {
        UUID internalId = UUID.fromString(authorId);

        checkAuthorExistsByIdOrThrow(internalId);
        authorRepository.deleteById(internalId);
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