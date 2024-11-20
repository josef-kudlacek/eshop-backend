package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.NotFoundException;
import cz.jkdabing.backend.mapper.ProductMapper;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.ProductService;
import cz.jkdabing.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final UserService userService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, UserService userService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userService = userService;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);
        UserEntity userEntity = userService.getCurrentUser();
        productEntity.setCreatedBy(userEntity);

        return productMapper.toDTO(productRepository.save(productEntity));
    }

    @Override
    public ProductEntity findProductByIdOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found, id was: " + productId));
    }

    @Override
    public void updateProduct(ProductEntity productEntity) {
        UserEntity userEntity = userService.getCurrentUser();
        productEntity.setUpdatedBy(userEntity);

        productRepository.save(productEntity);
    }
}
