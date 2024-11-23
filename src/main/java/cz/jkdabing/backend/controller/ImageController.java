package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.FileConstants;
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

    public ImageController(MessageService messageService,
                           ImageService imageService,
                           FileUploadProperties fileUploadProperties) {
        super(messageService);
        this.imageService = imageService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @PatchMapping("/{productId}/uploadImage")
    public ResponseEntity<String> saveImage(@PathVariable("productId") String productId,
                                            @RequestParam("image") MultipartFile imageFile) {
        checkImage(imageFile);
        try {
            imageService.saveImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return new ResponseEntity<>(getLocalizedMessage("image.uploaded"), HttpStatus.OK);
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{productId}/uploadImage")
    public ResponseEntity<String> updateImage(@PathVariable("productId") String productId,
                                              @RequestParam("image") MultipartFile imageFile) {
        checkImage(imageFile);
        try {
            imageService.updateImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return new ResponseEntity<>(getLocalizedMessage("image.uploaded"), HttpStatus.OK);
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}/deleteImage")
    public ResponseEntity<String> deleteImage(@PathVariable("productId") String productId) throws FileNotFoundException {
        imageService.deleteImage(productId);

        return new ResponseEntity<>(getLocalizedMessage("image.deleted"), HttpStatus.OK);
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
