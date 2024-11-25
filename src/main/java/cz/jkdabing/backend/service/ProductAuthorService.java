package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.ProductAuthorDTO;

public interface ProductAuthorService {

    void addAuthorToProduct(ProductAuthorDTO productAuthorDTO);

    void removeAuthorFromProduct(String authorProductId);
}
