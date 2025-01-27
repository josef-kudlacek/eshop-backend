package cz.jkdabing.backend.service;

import java.util.UUID;

public interface OrderService {

    void createOrder(UUID customerId);
}
