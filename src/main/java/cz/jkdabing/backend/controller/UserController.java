package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/activate/{userId}")
    public ResponseEntity<Void> activate(@PathVariable UUID userId) {
        userService.activeUser(userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok().build();
    }
}
