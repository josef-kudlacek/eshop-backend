package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;

import java.util.UUID;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductEntity findProductByIdOrThrow(UUID productId);

    void checkProductExistsByIdOrThrow(UUID productId);

    void updateProduct(ProductEntity productEntity);

    void addAuthorToProduct(UUID productId, AuthorProductDTO authorProductDTO);
}
