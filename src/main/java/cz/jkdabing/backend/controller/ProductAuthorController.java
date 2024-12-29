package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.ProductAuthorDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductAuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-author")
public class ProductAuthorController extends AbstractBaseController {

    private final ProductAuthorService productAuthorService;

    public ProductAuthorController(MessageService messageService, ProductAuthorService productAuthorService) {
        super(messageService);
        this.productAuthorService = productAuthorService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addAuthorToProduct(@Valid @RequestBody ProductAuthorDTO productAuthorDTO) {
        productAuthorService.addAuthorToProduct(productAuthorDTO);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.author.added")));
    }

    @DeleteMapping("/{productAuthorId}")
    public ResponseEntity<MessageResponse> removeAuthorFromProduct(@PathVariable String productAuthorId) {
        productAuthorService.removeAuthorFromProduct(productAuthorId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.author.removed")));
    }
}
