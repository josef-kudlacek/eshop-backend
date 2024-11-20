package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<String> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        authorService.createAuthor(authorDTO);

        return new ResponseEntity<>("Author created successfully", HttpStatus.OK);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<String> updateAuthor(@PathVariable String authorId, @RequestBody AuthorDTO authorDTO) {
        authorService.updateAuthor(authorId, authorDTO);

        return new ResponseEntity<>("Author updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<String> deleteAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);

        return new ResponseEntity<>("Author deleted successfully", HttpStatus.OK);
    }
}
