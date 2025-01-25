package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.request.ApplyCouponRequest;

import java.util.UUID;

public interface CartCouponService {

    CartDTO applyCoupon(UUID customerId, UUID cartId, ApplyCouponRequest applyCouponRequest);

    CartDTO removeCouponFromCart(UUID cartId, UUID customerId);

    CartDTO removeCouponFromCartItem(UUID cartId, UUID itemId, UUID customerId);

}
