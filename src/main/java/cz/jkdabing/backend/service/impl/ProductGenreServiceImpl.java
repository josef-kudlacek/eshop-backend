package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.ProductGenreDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.entity.ProductGenreEntity;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.ProductGenreMapper;
import cz.jkdabing.backend.repository.ProductGenreRepository;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductGenreService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ProductGenreServiceImpl extends AbstractService implements ProductGenreService {

    private final ProductGenreMapper productGenreMapper;

    private final ProductGenreRepository productGenreRepository;

    private final ProductRepository productRepository;

    public ProductGenreServiceImpl(
            MessageService messageService,
            AuditService auditService,
            ProductGenreMapper productGenreMapper,
            ProductGenreRepository productGenreRepository,
            ProductRepository productRepository
    ) {
        super(messageService, auditService);
        this.productGenreMapper = productGenreMapper;
        this.productGenreRepository = productGenreRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addGenreToProduct(ProductGenreDTO productGenreDTO) {
        try {
            ProductEntity productEntity = productRepository.getReferenceById(productGenreDTO.getProductId());
            ProductGenreEntity productGenreEntity = productGenreMapper.toEntity(productGenreDTO);

            productGenreEntity.setProduct(productEntity);

            productGenreRepository.save(productGenreEntity);

            prepareAuditLog(
                    TableNameUtil.getTableName(productGenreEntity.getClass()),
                    productGenreEntity.getGenreId(),
                    AuditLogConstants.ACTION_CREATE
            );
        } catch (DataIntegrityViolationException exception) {
            throw new NotFoundException(getLocalizedMessage("error.product.not.found", productGenreDTO.getProductId()));
        }
    }

    @Override
    public void removeGenreFromProduct(long genreId) {
        if (!productGenreRepository.existsById(genreId)) {
            throw new NotFoundException(getLocalizedMessage("error.genre.not.found", genreId));
        }

        productGenreRepository.deleteById(genreId);

        prepareAuditLog(
                TableNameUtil.getTableName(ProductGenreEntity.class),
                genreId,
                AuditLogConstants.ACTION_DELETE
        );
    }
}
