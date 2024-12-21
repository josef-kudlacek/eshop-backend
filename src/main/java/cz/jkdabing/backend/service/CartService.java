package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.CartItemDTO;

import java.util.UUID;

public interface CartService {

    CartDTO addItemToCart(UUID customerId, CartItemDTO cartItemDTO);

    CartDTO getCart(UUID customerId);

    void removeItemFromCart(UUID customerId, UUID cartItemId);

    void updateCartItemQuantity(UUID customerId, UUID cartId, UUID cartItemId, int quantity);

    void clearCart(UUID customerId, UUID cartId);
}
