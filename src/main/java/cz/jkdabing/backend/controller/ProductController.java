package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.ProductDetailDTO;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/products")
public class ProductController extends AbstractBaseController {

    private final ProductService productService;

    public ProductController(MessageService messageService, ProductService productService) {
        super(messageService);
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ProductDetailDTO getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @PutMapping("/{productId}")
    public ProductDTO updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductDTO productDTO
    ) {
        return productService.updateProduct(productId, productDTO);
    }
}
