package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PatchMapping("/{productId}/uploadImage")
    public ResponseEntity<String> saveImage(@PathVariable("productId") String productId,
                                            @RequestParam("image") MultipartFile imageFile) {
        try {
            imageService.saveImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{productId}/uploadImage")
    public ResponseEntity<String> updateImage(@PathVariable("productId") String productId,
                                              @RequestParam("image") MultipartFile imageFile) {
        try {
            imageService.updateImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}/deleteImage")
    public ResponseEntity<String> deleteImage(@PathVariable("productId") String productId) throws FileNotFoundException {
        imageService.deleteImage(productId);

        return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
    }
}
