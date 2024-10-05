package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        // TODO: Return exception message to requester
        userService.registerUser(userDTO);

        return ResponseEntity.ok("User registered successfully");
    }
}
