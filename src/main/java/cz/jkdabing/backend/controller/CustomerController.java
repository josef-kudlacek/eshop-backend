package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomerService customerService;

    private final SecurityService securityService;

    public CustomerController(
            JwtTokenProvider jwtTokenProvider,
            CustomerService customerService,
            SecurityService securityService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerService = customerService;
        this.securityService = securityService;
    }

    /**
     * Register a new customer billing information.
     *
     * @param token represents the customer JWT token
     * @param customerDTO represents the customer data for billing information
     *
     * @return the new payment JWT token in the response header
     */
    @PostMapping
    public ResponseEntity<Void> register(
            @RequestHeader(value = HttpHeaderConstants.AUTHORIZATION) String token,
            @Valid @RequestBody CustomerDTO customerDTO
    ) {
        UUID customerId = securityService.getCustomerId(token);
        customerService.createCustomer(customerDTO, customerId);
        token = jwtTokenProvider.createPaymentToken(customerId.toString());

        return ResponseEntity.ok()
                .header(HttpHeaderConstants.AUTHORIZATION, SecurityUtil.addBearerPrefix(token))
                .build();
    }
}
