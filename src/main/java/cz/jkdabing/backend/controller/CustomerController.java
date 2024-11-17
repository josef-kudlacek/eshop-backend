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

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerService customerService;

    public CustomerController(JwtTokenProvider jwtTokenProvider, CustomerService customerService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO) {
        UUID customerId = customerService.createCustomer(customerDTO);
        String token = jwtTokenProvider.createCustomerToken(customerId.toString());

        return ResponseEntity.ok(new JwtDTO(token));
    }
}
