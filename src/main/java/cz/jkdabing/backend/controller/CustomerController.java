package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController extends AbstractBaseController {

    private final CustomerService customerService;

    private final SecurityService securityService;

    public CustomerController(
            MessageService messageService,
            CustomerService customerService,
            SecurityService securityService
    ) {
        super(messageService);
        this.customerService = customerService;
        this.securityService = securityService;
    }

    /**
     * Register a new customer billing information.
     *
     * @param customerDTO represents the customer data for billing information
     * @return the new payment JWT token in the response cookie
     */
    @PostMapping
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody CustomerDTO customerDTO) {
        UUID customerId = securityService.getCurrentCustomerId();
        customerService.createCustomer(customerDTO, customerId);

        return ResponseEntity.ok()
                .body(new MessageResponse(getLocalizedMessage("customer.billing.info.success")));
    }
}
