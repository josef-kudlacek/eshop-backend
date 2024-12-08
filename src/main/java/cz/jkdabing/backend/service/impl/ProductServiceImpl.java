package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.ProductMapper;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductService;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl extends AbstractService implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(
            MessageService messageService,
            AuditService auditService,
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        super(messageService, auditService);
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @CacheEvict(value = "activeProducts", allEntries = true)
    public ProductDTO createProduct(@Valid ProductDTO productDTO) {
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
    public ProductEntity findProductByIdOrThrow(@NotNull UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.product.not.found", productId)
                ));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "activeProducts", key = "#productId"),
            @CacheEvict(value = "productDetails", key = "#productId")
    })
    public ProductDTO updateProduct(@NotEmpty UUID productId, @Valid ProductDTO productDTO) {
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
    @Caching(evict = {
            @CacheEvict(value = "activeProducts", key = "#productEntity.productId"),
            @CacheEvict(value = "productDetails", key = "#productEntity.productId")
    })
    public void updateProduct(ProductEntity productEntity) {
        productRepository.save(productEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(ProductEntity.class),
                productEntity.getProductId(),
                AuditLogConstants.ACTION_UPDATE
        );
    }

    @Override
    @Cacheable(value = "productDetails", key = "#productId")
    public ProductDTO getProduct(@NotEmpty UUID productId) {
        ProductEntity productEntityWithDetail = productRepository.findProductDetailByProductId(productId)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.product.not.found", productId)
                ));

        return productMapper.toDTO(productEntityWithDetail);
    }

    @Override
    @Cacheable(value = "activeProducts", unless = "#result.size() == 0")
    public List<ProductDTO> getActiveProducts() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        List<ProductEntity> productEntities = productRepository.findActiveProducts(currentDateTime);

        return productMapper.toDTOList(productEntities);
    }
}
