package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.exception.BadRequestException;
import cz.jkdabing.backend.service.ImageService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.FileValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ImageController extends AbstractBaseController {

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

    @PatchMapping("/{productId}/uploadImage")
    public ResponseEntity<MessageResponse> saveImage(
            @PathVariable("productId") String productId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        checkImage(imageFile);
        try {
            imageService.saveImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("image.uploaded")));
        } catch (IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.server.side")));
        }
    }

    @PutMapping("/{productId}/uploadImage")
    public ResponseEntity<MessageResponse> updateImage(
            @PathVariable("productId") String productId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        checkImage(imageFile);
        try {
            imageService.updateImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("image.uploaded")));
        } catch (IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.server.side")));
        }
    }

    @DeleteMapping("/{productId}/deleteImage")
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
