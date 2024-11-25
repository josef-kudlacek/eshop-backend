package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.ProductFormatDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.entity.ProductFormatEntity;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.ProductFormatMapper;
import cz.jkdabing.backend.repository.ProductFormatRepository;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductFormatService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductFormatServiceImpl extends AbstractService implements ProductFormatService {

    private final ProductRepository productRepository;

    private final ProductFormatRepository productFormatRepository;

    private final ProductFormatMapper productFormatMapper;

    public ProductFormatServiceImpl(
            MessageService messageService,
            AuditService auditService,
            ProductRepository productRepository,
            ProductFormatRepository productFormatRepository,
            ProductFormatMapper productFormatMapper
    ) {
        super(messageService, auditService);
        this.productRepository = productRepository;
        this.productFormatRepository = productFormatRepository;
        this.productFormatMapper = productFormatMapper;
    }

    @Override
    public void addFormatToProduct(ProductFormatDTO productFormatDTO) {
        UUID productId = productFormatDTO.getProductId();
        try {
            ProductEntity productEntity = productRepository.getReferenceById(productId);
            ProductFormatEntity productFormatEntity = productFormatMapper.toEntity(productFormatDTO);
            productFormatEntity.setProduct(productEntity);

            productFormatRepository.save(productFormatEntity);

            prepareAuditLog(
                    TableNameUtil.getTableName(productFormatEntity.getClass()),
                    productFormatEntity.getFormatId().longValue(),
                    AuditLogConstants.ACTION_CREATE
            );
        } catch (DataIntegrityViolationException exception) {
            throw new NotFoundException(getLocalizedMessage("error.product.not.found", productId));
        }
    }

    @Override
    public void deleteFormatFromProduct(int formatId) {
        if (!productFormatRepository.existsById(formatId)) {
            throw new NotFoundException(getLocalizedMessage("error.format.not.found", formatId));
        }

        productFormatRepository.deleteById(formatId);

        prepareAuditLog(
                TableNameUtil.getTableName(ProductFormatEntity.class),
                Integer.toUnsignedLong(formatId),
                AuditLogConstants.ACTION_DELETE
        );
    }
}
