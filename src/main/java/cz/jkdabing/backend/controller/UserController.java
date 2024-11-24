package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.JwtDTO;
import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractBaseController {

    private final UserService userService;

    private final SecurityService securityService;

    public UserController(MessageService messageService, UserService userService, SecurityService securityService) {
        super(messageService);
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/activate")
    public ResponseEntity<Void> activate(@RequestParam String token) {
        try {
            userService.activeUser(token);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = userService.authenticateUser(loginDTO);
            return ResponseEntity.ok(new JwtDTO(token));
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getLocalizedMessage("error.user.not.found"));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(getLocalizedMessage(exception.getMessage()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(getLocalizedMessage("error.occurred"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        securityService.logoutUser();

        return ResponseEntity.status(HttpStatus.OK)
                .body(getLocalizedMessage("user.logout"));
    }
}
