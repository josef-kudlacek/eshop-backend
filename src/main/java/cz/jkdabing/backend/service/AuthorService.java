package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.entity.AuthorEntity;

import java.util.UUID;

public interface AuthorService {

    void createAuthor(AuthorDTO authorDTO);

    void updateAuthor(String authorId, AuthorDTO authorDTO);

    void deleteAuthor(String authorId);

    void checkAuthorExistsByIdOrThrow(UUID authorId);

    AuthorEntity findAuthorByIdOrThrow(UUID authorId);
}
