package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import cz.jkdabing.backend.entity.AuthorProductEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.NotFoundException;
import cz.jkdabing.backend.mapper.AuthorProductMapper;
import cz.jkdabing.backend.mapper.ProductMapper;
import cz.jkdabing.backend.repository.AuthorProductRepository;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.AuthorService;
import cz.jkdabing.backend.service.ProductService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final AuditService auditService;

    private final AuthorService authorService;

    private final AuthorProductRepository authorProductRepository;

    private final AuthorProductMapper authorProductMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              AuditService auditService, AuthorService authorService,
                              AuthorProductRepository authorProductRepository,
                              AuthorProductMapper authorProductMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.auditService = auditService;
        this.authorService = authorService;
        this.authorProductRepository = authorProductRepository;
        this.authorProductMapper = authorProductMapper;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);
        productRepository.save(productEntity);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(productEntity.getClass()),
                productEntity.getProductId(),
                AuditLogConstants.ACTION_CREATE
        );

        return productMapper.toDTO(productEntity);
    }

    @Override
    public ProductEntity findProductByIdOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found, id was: " + productId));
    }

    @Override
    public void checkProductExistsByIdOrThrow(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found, id was: " + productId);
        }
    }

    @Override
    public void updateProduct(ProductEntity productEntity) {
        productRepository.save(productEntity);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(productEntity.getClass()),
                productEntity.getProductId(),
                AuditLogConstants.ACTION_UPDATE
        );
    }

    @Override
    public void addAuthorToProduct(UUID productId, AuthorProductDTO authorProductDTO) {
        AuthorEntity authorEntity = authorService.findAuthorByIdOrThrow(authorProductDTO.getAuthorId());
        ProductEntity productEntity = findProductByIdOrThrow(productId);

        AuthorProductEntity authorProductEntity = authorProductMapper.toEntity(authorProductDTO);
        authorProductEntity.setProduct(productEntity);
        authorProductEntity.setAuthor(authorEntity);

        authorProductRepository.save(authorProductEntity);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(authorProductEntity.getClass()),
                authorProductEntity.getAuthorProductId(),
                AuditLogConstants.ACTION_CREATE
        );
    }
}
