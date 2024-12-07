package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.ProductBaseDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.ProductDetailDTO;
import cz.jkdabing.backend.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductEntity findProductByIdOrThrow(UUID productId);

    ProductDTO updateProduct(UUID productId, ProductDTO productDTO);

    void updateProduct(ProductEntity productEntity);

    ProductDetailDTO getProduct(UUID productId);

    List<ProductBaseDTO> getActiveProducts();
}
