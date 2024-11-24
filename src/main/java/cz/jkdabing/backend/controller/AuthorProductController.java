package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.AuthorProductService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authorProduct")
public class AuthorProductController extends AbstractBaseController {

    private final AuthorProductService authorProductService;

    public AuthorProductController(MessageService messageService, AuthorProductService authorProductService) {
        super(messageService);
        this.authorProductService = authorProductService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addAuthorToProduct(@Valid @RequestBody AuthorProductDTO authorProductDTO) {
        authorProductService.addAuthorToProduct(authorProductDTO);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("author.added")));
    }

    @DeleteMapping("/{authorProductId}")
    public ResponseEntity<MessageResponse> removeAuthorFromProduct(@PathVariable String authorProductId) {
        authorProductService.removeAuthorFromProduct(authorProductId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("author.removed")));
    }
}
