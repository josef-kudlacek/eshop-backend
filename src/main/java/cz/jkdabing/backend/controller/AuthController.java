package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.dto.JwtDTO;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerService customerService;

    public AuthController(JwtTokenProvider jwtTokenProvider, CustomerService customerService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO) {
        Long customerId = customerService.registerCustomer(customerDTO);
        String token = jwtTokenProvider.createToken(customerId.toString());

        return ResponseEntity.ok(new JwtDTO(token));
    }
}
