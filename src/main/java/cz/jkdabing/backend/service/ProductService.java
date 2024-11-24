package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;

import java.util.UUID;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductEntity findProductByIdOrThrow(UUID productId);

    ProductDTO updateProduct(UUID productId, ProductDTO productDTO);

    void updateProduct(ProductEntity productEntity);

}
