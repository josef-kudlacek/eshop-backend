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
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl extends AbstractService implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final AuthorService authorService;

    private final AuthorProductRepository authorProductRepository;

    private final AuthorProductMapper authorProductMapper;

    public ProductServiceImpl(
            MessageService messageService,
            AuditService auditService, AuthorService authorService,
            ProductRepository productRepository, ProductMapper productMapper,
            AuthorProductRepository authorProductRepository,
            AuthorProductMapper authorProductMapper) {
        super(messageService, auditService);
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.authorService = authorService;
        this.authorProductRepository = authorProductRepository;
        this.authorProductMapper = authorProductMapper;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);
        productRepository.save(productEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(productEntity.getClass()),
                productEntity.getProductId(),
                AuditLogConstants.ACTION_CREATE
        );

        return productMapper.toDTO(productEntity);
    }

    @Override
    public ProductEntity findProductByIdOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.product.not.found", productId)
                ));
    }

    @Override
    public ProductDTO updateProduct(UUID productId, ProductDTO productDTO) {
        ProductEntity productEntity = findProductByIdOrThrow(productId);
        productDTO.setProductId(productId);
        productEntity = productMapper.updateEntity(productDTO, productEntity);
        productRepository.save(productEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(productEntity.getClass()),
                productEntity.getProductId(),
                AuditLogConstants.ACTION_UPDATE
        );

        return productMapper.toDTO(productEntity);
    }

    @Override
    public void updateProduct(ProductEntity productEntity) {
        productRepository.save(productEntity);

        prepareAuditLog(
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

        prepareAuditLog(
                TableNameUtil.getTableName(authorProductEntity.getClass()),
                authorProductEntity.getAuthorProductId(),
                AuditLogConstants.ACTION_CREATE
        );
    }
}
