package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.OrderService;
import cz.jkdabing.backend.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends AbstractBaseController {

    private final SecurityService securityService;

    private final OrderService orderService;

    protected OrderController(
            MessageService messageService,
            SecurityService securityService,
            OrderService orderService
    ) {
        super(messageService);
        this.securityService = securityService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder() {
        UUID customerId = securityService.getCurrentCustomerId();
        orderService.createOrder(customerId);

        return ResponseEntity.ok().build();
    }
}
