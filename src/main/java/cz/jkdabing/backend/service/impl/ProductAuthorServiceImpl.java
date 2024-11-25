package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.ProductAuthorDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import cz.jkdabing.backend.entity.ProductAuthorEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.ProductAuthorMapper;
import cz.jkdabing.backend.repository.AuthorRepository;
import cz.jkdabing.backend.repository.ProductAuthorRepository;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductAuthorService;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductAuthorServiceImpl extends AbstractService implements ProductAuthorService {

    private final ProductAuthorRepository productAuthorRepository;

    private final ProductAuthorMapper productAuthorMapper;

    private final AuthorRepository authorRepository;

    private final ProductRepository productRepository;

    public ProductAuthorServiceImpl(
            MessageService messageService,
            AuditService auditService,
            ProductAuthorRepository productAuthorRepository,
            ProductAuthorMapper productAuthorMapper,
            AuthorRepository authorRepository,
            ProductRepository productRepository
    ) {
        super(messageService, auditService);
        this.productAuthorRepository = productAuthorRepository;
        this.productAuthorMapper = productAuthorMapper;
        this.authorRepository = authorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addAuthorToProduct(ProductAuthorDTO productAuthorDTO) {
        try {
            AuthorEntity authorEntity = authorRepository.getReferenceById(productAuthorDTO.getAuthorId());
            ProductEntity productEntity = productRepository.getReferenceById(productAuthorDTO.getProductId());

            ProductAuthorEntity productAuthorEntity = productAuthorMapper.toEntity(productAuthorDTO);

            productAuthorEntity.setAuthor(authorEntity);
            productAuthorEntity.setProduct(productEntity);
            productAuthorRepository.save(productAuthorEntity);

            prepareAuditLog(
                    TableNameUtil.getTableName(productAuthorEntity.getClass()),
                    productAuthorEntity.getProductAuthorId(),
                    AuditLogConstants.ACTION_CREATE
            );
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(getLocalizedMessage("error.author.or.product.not.exist"));
        }
    }

    @Override
    public void removeAuthorFromProduct(String authorProductId) {
        UUID internalId = UUID.fromString(authorProductId);
        if (!productAuthorRepository.existsById(internalId)) {
            throw new BadRequestException(getLocalizedMessage("error.product.author.relationship.not.exist"));
        }

        productAuthorRepository.deleteById(internalId);

        prepareAuditLog(
                TableNameUtil.getTableName(ProductAuthorEntity.class),
                internalId,
                AuditLogConstants.ACTION_DELETE
        );
    }
}
