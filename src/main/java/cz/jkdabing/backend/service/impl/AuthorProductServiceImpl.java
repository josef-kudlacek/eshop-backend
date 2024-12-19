package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import cz.jkdabing.backend.entity.AuthorProductEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.AuthorProductMapper;
import cz.jkdabing.backend.repository.AuthorProductRepository;
import cz.jkdabing.backend.repository.AuthorRepository;
import cz.jkdabing.backend.repository.ProductRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.AuthorProductService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorProductServiceImpl extends AbstractService implements AuthorProductService {

    private final AuthorProductRepository authorProductRepository;

    private final AuthorProductMapper authorProductMapper;

    private final AuthorRepository authorRepository;

    private final ProductRepository productRepository;

    public AuthorProductServiceImpl(
            MessageService messageService,
            AuditService auditService,
            AuthorProductRepository authorProductRepository,
            AuthorProductMapper authorProductMapper,
            AuthorRepository authorRepository,
            ProductRepository productRepository
    ) {
        super(messageService, auditService);
        this.authorProductRepository = authorProductRepository;
        this.authorProductMapper = authorProductMapper;
        this.authorRepository = authorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addAuthorToProduct(AuthorProductDTO authorProductDTO) {
        AuthorProductEntity authorProductEntity = authorProductMapper.toEntity(authorProductDTO);
        try {
            AuthorEntity authorEntity = authorRepository.getReferenceById(authorProductDTO.getAuthorId());
            ProductEntity productEntity = productRepository.getReferenceById(authorProductDTO.getProductId());

            authorProductEntity.setAuthor(authorEntity);
            authorProductEntity.setProduct(productEntity);
            authorProductRepository.save(authorProductEntity);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException(getLocalizedMessage("error.author.or.product.not.exist"));
        }

        prepareAuditLog(
                TableNameUtil.getTableName(authorProductEntity.getClass()),
                authorProductEntity.getAuthorProductId(),
                AuditLogConstants.ACTION_CREATE
        );
    }

    @Override
    public void removeAuthorFromProduct(String authorProductId) {
        UUID internalId = UUID.fromString(authorProductId);
        if (!authorProductRepository.existsById(internalId)) {
            throw new BadRequestException(getLocalizedMessage("error.author.product.not.exist"));
        }

        authorProductRepository.deleteById(internalId);

        prepareAuditLog(
                TableNameUtil.getTableName(AuthorProductEntity.class),
                internalId,
                AuditLogConstants.ACTION_DELETE
        );
    }
}
