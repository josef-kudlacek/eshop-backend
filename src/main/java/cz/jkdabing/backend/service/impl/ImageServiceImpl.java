package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.configuration.FileStorageProperties;
import cz.jkdabing.backend.entity.ImageEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.repository.ImageRepository;
import cz.jkdabing.backend.service.ImageService;
import cz.jkdabing.backend.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ProductService productService;

    private final ImageRepository imageRepository;

    private final FileStorageProperties fileStorageProperties;

    public ImageServiceImpl(ProductService productService, ImageRepository imageRepository, FileStorageProperties fileStorageProperties) {
        this.productService = productService;
        this.imageRepository = imageRepository;
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public void saveImage(String productId, MultipartFile image, String imagePath) throws IOException {
        ProductEntity productEntity = productService.findProductByIdOrThrow(UUID.fromString(productId));

        ImageEntity imageEntity = prepareImage(image);
        imageEntity.setImageUrl(imagePath);
        imageEntity.setProduct(productEntity);

        Path uploadPath = Paths.get(fileStorageProperties.getUploadDirectory() + imagePath, imageEntity.getImageName());
        Files.write(uploadPath, image.getBytes());

        imageRepository.save(imageEntity);
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
}
