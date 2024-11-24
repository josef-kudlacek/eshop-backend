package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.AuthorService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController extends AbstractBaseController {

    private final AuthorService authorService;

    public AuthorController(MessageService messageService, AuthorService authorService) {
        super(messageService);
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDTO> getAuthors() {
        return authorService.getAuthors();
    }

    @PostMapping
    public MessageResponse createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        authorService.createAuthor(authorDTO);

        return new MessageResponse(getLocalizedMessage("author.created"));
    }

    @PutMapping("/{authorId}")
    public MessageResponse updateAuthor(@PathVariable String authorId, @RequestBody AuthorDTO authorDTO) {
        authorService.updateAuthor(authorId, authorDTO);

        return new MessageResponse(getLocalizedMessage("author.updated"));
    }

    @DeleteMapping("/{authorId}")
    public MessageResponse deleteAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);

        return new MessageResponse(getLocalizedMessage("author.deleted"));
    }
}
