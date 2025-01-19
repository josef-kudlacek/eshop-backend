package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.controller.AbstractBaseController;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.response.ProductAdminDTO;
import cz.jkdabing.backend.mapper.response.ProductResponseMapper;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController extends AbstractBaseController {

    private final ProductService productService;

    private final ProductResponseMapper productResponseMapper;

    public AdminProductController(
            MessageService messageService,
            ProductService productService,
            ProductResponseMapper productResponseMapper
    ) {
        super(messageService);
        this.productService = productService;
        this.productResponseMapper = productResponseMapper;
    }

    @GetMapping("/{productId}")
    public ProductAdminDTO getProduct(@PathVariable UUID productId) {
        ProductDTO productDTO = productService.getProduct(productId);
        return productResponseMapper.toProductAdminResponse(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductAdminDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        ProductAdminDTO productAdminResponse = productResponseMapper.toProductAdminResponse(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productAdminResponse);
    }

    @PutMapping("/{productId}")
    public ProductAdminDTO updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductDTO productDTO
    ) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return productResponseMapper.toProductAdminResponse(updatedProduct);
    }
}
