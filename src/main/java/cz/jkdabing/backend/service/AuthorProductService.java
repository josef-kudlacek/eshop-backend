package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.AuthorProductDTO;

public interface AuthorProductService {

    void addAuthorToProduct(AuthorProductDTO authorProductDTO);

    void removeAuthorFromProduct(String authorProductId);
}
