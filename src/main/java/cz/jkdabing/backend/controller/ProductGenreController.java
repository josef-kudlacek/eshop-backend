package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.ProductGenreDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductGenreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/genreProduct")
public class ProductGenreController extends AbstractBaseController {

    private final ProductGenreService genreProductService;

    public ProductGenreController(MessageService messageService, ProductGenreService genreProductService) {
        super(messageService);
        this.genreProductService = genreProductService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addAuthorToProduct(@Valid @RequestBody ProductGenreDTO productGenreDTO) {
        genreProductService.addGenreToProduct(productGenreDTO);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("product.genre.added")));
    }
}
