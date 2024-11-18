package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.service.ImageService;
import cz.jkdabing.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final ImageService imageService;

    public ProductController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/uploadImage")
    public ResponseEntity<String> uploadImage(@PathVariable("productId") String productId,
                                              @RequestParam("image") MultipartFile imageFile) throws IOException {
        imageService.saveImage(productId, imageFile, FileConstants.PRODUCT_IMAGE_RELATIVE_PATH);

        return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
    }
}
