package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.entity.ImageEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.ImageAlreadyExistsException;
import cz.jkdabing.backend.repository.ImageRepository;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.FileUtils;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl extends AbstractService implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ProductService productService;

    private final ImageRepository imageRepository;

    private final FileUploadProperties fileUploadProperties;

    public ImageServiceImpl(
            MessageService messageService,
            ProductService productService,
            AuditService auditService,
            ImageRepository imageRepository,
            FileUploadProperties fileUploadProperties) {
        super(messageService, auditService);
        this.productService = productService;
        this.imageRepository = imageRepository;
        this.fileUploadProperties = fileUploadProperties;
    }

    @Override
    @Transactional
    public void saveImage(String productId, MultipartFile image, String imagePath) throws IOException {
        ProductEntity productEntity = productService.findProductByIdOrThrow(UUID.fromString(productId));

        if (productEntity.getImage() != null) {
            String productAlreadyHasImage = getLocalizedMessage("error.product.has.image", productEntity.getProductName());
            throw new ImageAlreadyExistsException(productAlreadyHasImage);
        }

        uploadAndSaveImage(productEntity, image, imagePath);
    }

    @Override
    @Transactional
    public void updateImage(String productId, MultipartFile image, String imagePath) throws IOException {
        ProductEntity productEntity = productService.findProductByIdOrThrow(UUID.fromString(productId));

        checkAndRemoveImage(productEntity);

        uploadAndSaveImage(productEntity, image, imagePath);
    }

    @Override
    @Transactional
    public void deleteImage(String productId) {
        ProductEntity productEntity = productService.findProductByIdOrThrow(UUID.fromString(productId));

        checkAndRemoveImage(productEntity);
    }

    private ImageEntity prepareImage(@NotNull MultipartFile image) {
        String fileName = image.getOriginalFilename();
        String fileFormat = FileUtils.getFileExtension(fileName);

        return ImageEntity.builder()
                .imageName(fileName)
                .imageFormatType(fileFormat)
                .build();
    }

    private void uploadAndSaveImage(
            ProductEntity productEntity,
            @NotNull MultipartFile image,
            @NotEmpty String imagePath
    ) throws IOException {
        ImageEntity imageEntity = prepareImage(image);
        uploadImageFile(image, imagePath, imageEntity.getImageName());

        imageEntity.setImageUrl(imagePath);
        imageRepository.save(imageEntity);

        productEntity.setImage(imageEntity);
        productService.updateProduct(productEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(imageEntity.getClass()),
                imageEntity.getImageId(),
                AuditLogConstants.ACTION_UPLOAD
        );
    }

    private void uploadImageFile(
            @NotNull MultipartFile image,
            @NotEmpty String imagePath,
            @NotEmpty String imageName
    ) throws IOException {
        Path uploadPath = Paths.get(fileUploadProperties.getUploadPublicDirectory() + imagePath, imageName);
        Files.write(uploadPath, image.getBytes());
    }

    private void checkAndRemoveImage(ProductEntity productEntity) {
        ImageEntity imageEntity = productEntity.getImage();
        if (imageEntity != null) {
            deleteImageFile(imageEntity.getImageUrl(), imageEntity.getImageName());

            productEntity.setImage(null);
            productService.updateProduct(productEntity);
            imageRepository.delete(imageEntity);

            prepareAuditLog(
                    AuditLogConstants.TABLE_NAME_IMAGES,
                    imageEntity.getImageId(),
                    AuditLogConstants.ACTION_DELETE
            );
        } else {
            String productWithoutImage = getLocalizedMessage("error.product.without.image", productEntity.getProductName());
            throw new EntityNotFoundException(productWithoutImage);
        }
    }

    private void deleteImageFile(
            @NotEmpty String imagePath,
            @NotEmpty String imageName
    ) {
        Path path = Paths.get(fileUploadProperties.getUploadPublicDirectory() + imagePath, imageName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException ioException) {
            logger.warn("File cannot be deleted: ", ioException);
        }
    }
}
