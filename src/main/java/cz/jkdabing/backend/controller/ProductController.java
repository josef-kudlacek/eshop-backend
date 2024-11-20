package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/addAuthor")
    public ResponseEntity<String> addAuthorToProduct(@PathVariable UUID productId,
                                                     @Valid @RequestBody AuthorProductDTO authorProductDTO) {
        productService.addAuthorToProduct(productId, authorProductDTO);

        return new ResponseEntity<>("Author added to product successfully", HttpStatus.OK);
    }
}
