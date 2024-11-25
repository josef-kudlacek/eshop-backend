package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.ProductGenreDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductGenreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-genre")
public class ProductGenreController extends AbstractBaseController {

    private final ProductGenreService genreProductService;

    public ProductGenreController(MessageService messageService, ProductGenreService genreProductService) {
        super(messageService);
        this.genreProductService = genreProductService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addGenreToProduct(@Valid @RequestBody ProductGenreDTO productGenreDTO) {
        genreProductService.addGenreToProduct(productGenreDTO);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.genre.added")));
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<MessageResponse> removeGenreFromProduct(@PathVariable Long genreId) {
        genreProductService.removeGenreFromProduct(genreId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.genre.removed")));
    }
}
