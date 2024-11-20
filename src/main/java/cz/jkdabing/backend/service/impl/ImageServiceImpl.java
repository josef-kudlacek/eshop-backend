package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.configuration.FileStorageProperties;
import cz.jkdabing.backend.entity.ImageEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.ImageAlreadyExistsException;
import cz.jkdabing.backend.repository.ImageRepository;
import cz.jkdabing.backend.service.ImageService;
import cz.jkdabing.backend.service.ProductService;
import cz.jkdabing.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ProductService productService;

    private final UserService userService;

    private final ImageRepository imageRepository;

    private final FileStorageProperties fileStorageProperties;

    public ImageServiceImpl(ProductService productService, UserService userService,
                            ImageRepository imageRepository, FileStorageProperties fileStorageProperties) {
        this.productService = productService;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    @Transactional
    public void saveImage(String productId, MultipartFile image, String imagePath) throws IOException {
        ProductEntity productEntity = productService.findProductByIdOrThrow(UUID.fromString(productId));

        if (productEntity.getImage() != null) {
            throw new ImageAlreadyExistsException(
                    String.format("Product '%s' already has an image", productEntity.getProductName())
            );
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

    private ImageEntity prepareImage(MultipartFile image) {
        String fileName = image.getOriginalFilename();
        String fileFormat = getFileExtension(fileName);

        return ImageEntity.builder()
                .imageName(fileName)
                .imageFormatType(fileFormat)
                .build();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return null;
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void uploadAndSaveImage(ProductEntity productEntity, MultipartFile image, String imagePath) throws IOException {
        ImageEntity imageEntity = prepareImage(image);
        uploadImageFile(image, imagePath, imageEntity.getImageName());

        imageEntity.setImageUrl(imagePath);
        UserEntity userEntity = userService.getCurrentUser();
        imageEntity.setCreatedBy(userEntity);
        imageRepository.save(imageEntity);

        productEntity.setImage(imageEntity);
        productService.updateProduct(productEntity);
    }

    private void uploadImageFile(MultipartFile image, String imagePath, String imageName) throws IOException {
        Path uploadPath = Paths.get(fileStorageProperties.getUploadDirectory() + imagePath, imageName);
        Files.write(uploadPath, image.getBytes());
    }

    private void checkAndRemoveImage(ProductEntity productEntity) {
        ImageEntity imageEntity = productEntity.getImage();
        if (imageEntity != null) {
            deleteImageFile(imageEntity.getImageUrl(), imageEntity.getImageName());

            productEntity.setImage(null);
            productService.updateProduct(productEntity);
            imageRepository.delete(imageEntity);
        } else {
            throw new EntityNotFoundException(String.format("Product '%s' does not have an image", productEntity.getProductName()));
        }
    }

    private void deleteImageFile(String imagePath, String imageName) {
        Path path = Paths.get(fileStorageProperties.getUploadDirectory() + imagePath, imageName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
