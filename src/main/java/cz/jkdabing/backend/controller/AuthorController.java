package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.service.AuthorService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorController extends AbstractBaseController {

    private final AuthorService authorService;

    public AuthorController(MessageService messageService, AuthorService authorService) {
        super(messageService);
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<String> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        authorService.createAuthor(authorDTO);

        return new ResponseEntity<>(getLocalizedMessage("author.created"), HttpStatus.OK);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<String> updateAuthor(@PathVariable String authorId, @RequestBody AuthorDTO authorDTO) {
        authorService.updateAuthor(authorId, authorDTO);

        return new ResponseEntity<>(getLocalizedMessage("author.updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<String> deleteAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);

        return new ResponseEntity<>(getLocalizedMessage("author.deleted"), HttpStatus.OK);
    }
}
