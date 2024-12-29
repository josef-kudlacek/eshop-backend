package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.controller.AbstractBaseController;
import cz.jkdabing.backend.dto.ProductFormatDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductFormatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product-format")
public class ProductFormatController extends AbstractBaseController {

    private final ProductFormatService productFormatService;

    public ProductFormatController(MessageService messageService, ProductFormatService productFormatService) {
        super(messageService);
        this.productFormatService = productFormatService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addAuthorToProduct(@Valid @RequestBody ProductFormatDTO productFormatDTO) {
        productFormatService.addFormatToProduct(productFormatDTO);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.format.added")));
    }

    @DeleteMapping("/{formatId}")
    public ResponseEntity<MessageResponse> deleteAuthorFromProduct(@PathVariable Integer formatId) {
        productFormatService.deleteFormatFromProduct(formatId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.format.removed")));
    }
}

