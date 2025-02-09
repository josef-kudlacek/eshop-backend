package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.ResponseConstants;
import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.security.config.SecurityConfig;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.service.UserService;
import cz.jkdabing.backend.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    private final SecurityConfig securityConfig;

    public UserController(
            MessageService messageService,
            UserService userService,
            SecurityService securityService,
            SecurityConfig securityConfig
    ) {
        super(messageService);
        this.userService = userService;
        this.securityService = securityService;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("user.registration.success")));
    }

    @GetMapping("/activate")
    public ResponseEntity<MessageResponse> activate(@RequestParam String token) {
        try {
            userService.activeUser(token);
            return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("user.activation")));
        } catch (NoSuchElementException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(getLocalizedMessage("error.user.activation.not.found")));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        try {
            String token = userService.authenticateUser(loginDTO);

            Cookie cookie = SecurityUtil.createCookie(ResponseConstants.COOKIE_ACCESS_TOKEN, token, securityConfig.getUserAccessTokenExpirationSeconds());
            httpServletResponse.addCookie(cookie);

            return ResponseEntity.ok()
                    .body(new MessageResponse(getLocalizedMessage("user.login.success")));
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(getLocalizedMessage("error.user.not.found")));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(getLocalizedMessage(exception.getMessage())));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.occurred")));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        securityService.logoutUser();

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("user.logout")));
    }
}
