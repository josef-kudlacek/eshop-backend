package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.service.ImageService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.FileValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ImageController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    private final FileUploadProperties fileUploadProperties;

    public ImageController(
            MessageService messageService,
            ImageService imageService,
            FileUploadProperties fileUploadProperties
    ) {
        super(messageService);
        this.imageService = imageService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @PatchMapping("/{productId}/upload-image")
    public ResponseEntity<MessageResponse> saveImage(
            @PathVariable("productId") String productId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        checkImage(imageFile);
        try {
            imageService.saveImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("image.uploaded")));
        } catch (IOException exception) {
            logger.error("Image was not saved correctly", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.server.side")));
        }
    }

    @PutMapping("/{productId}/upload-image")
    public ResponseEntity<MessageResponse> updateImage(
            @PathVariable("productId") String productId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        checkImage(imageFile);
        try {
            imageService.updateImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("image.uploaded")));
        } catch (IOException exception) {
            logger.error("Image was not updated correctly", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.server.side")));
        }
    }

    @DeleteMapping("/{productId}/delete-image")
    public ResponseEntity<MessageResponse> deleteImage(
            @PathVariable("productId") String productId
    ) throws FileNotFoundException {
        imageService.deleteImage(productId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("image.deleted")));
    }

    private void checkImage(MultipartFile imageFile) {
        checkImageIsNotEmpty(imageFile);
        checkImageExtension(imageFile);
        checkImageSize(imageFile);

    }

    private void checkImageIsNotEmpty(MultipartFile imageFile) {
        boolean imageIsEmpty = imageFile == null || imageFile.isEmpty();
        if (imageIsEmpty) {
            throw new BadRequestException(getLocalizedMessage("error.image.empty.image"));
        }
    }

    private void checkImageExtension(MultipartFile imageFile) {
        boolean imageFileIsNotValid = !FileValidationUtils.isImageFileValid(imageFile.getOriginalFilename());
        if (imageFileIsNotValid) {
            throw new BadRequestException(getLocalizedMessage("error.image.invalid.extension"));
        }
    }

    private void checkImageSize(MultipartFile imageFile) {
        boolean imageFileIsTooLarge = imageFile.getSize() > fileUploadProperties.getMaxImageFileSize();
        if (imageFileIsTooLarge) {
            throw new BadRequestException(getLocalizedMessage("error.image.size.exceed.limit"));
        }
    }
}
