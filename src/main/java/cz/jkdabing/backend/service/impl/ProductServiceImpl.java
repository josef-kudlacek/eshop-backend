package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.ProductDetailDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.ProductMapper;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

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
                TableNameUtil.getTableName(ProductEntity.class),
                productEntity.getProductId(),
                AuditLogConstants.ACTION_UPDATE
        );
    }

    @Override
    public ProductDetailDTO getProduct(UUID productId) {
        ProductEntity productEntityWithDetail = productRepository.findProductDetailByProductId(productId)
                .orElseThrow(() -> new NotFoundException(
                        getLocalizedMessage("error.product.not.found", productId)
                ));

        return productMapper.toDetailDTO(productEntityWithDetail);
    }
}
