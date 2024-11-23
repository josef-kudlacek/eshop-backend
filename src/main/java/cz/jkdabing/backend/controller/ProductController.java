package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.dto.ProductDTO;
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

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/addAuthor")
    public ResponseEntity<String> addAuthorToProduct(@PathVariable UUID productId,
                                                     @Valid @RequestBody AuthorProductDTO authorProductDTO) {
        productService.addAuthorToProduct(productId, authorProductDTO);

        return new ResponseEntity<>(getLocalizedMessage("author.added"), HttpStatus.OK);
    }
}
