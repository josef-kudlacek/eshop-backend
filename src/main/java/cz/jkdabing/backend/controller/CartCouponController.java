package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.request.ApplyCouponRequest;
import cz.jkdabing.backend.dto.response.CartResponse;
import cz.jkdabing.backend.mapper.response.CartResponseMapper;
import cz.jkdabing.backend.service.CartCouponService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart-coupon")
public class CartCouponController extends AbstractBaseController {

    private final CartCouponService cartCouponService;

    private final CartResponseMapper cartResponseMapper;

    private final SecurityService securityService;

    public CartCouponController(
            MessageService messageService,
            CartCouponService cartCouponService,
            CartResponseMapper cartResponseMapper,
            SecurityService securityService
    ) {
        super(messageService);
        this.cartCouponService = cartCouponService;
        this.cartResponseMapper = cartResponseMapper;
        this.securityService = securityService;
    }

    @PostMapping("/{cartId}")
    public ResponseEntity<CartResponse> applyCoupon(
            @PathVariable UUID cartId,
            @Valid @RequestBody ApplyCouponRequest applyCouponRequest
    ) {
        UUID customerId = securityService.getCurrentCustomerId();
        CartDTO cartDTO = cartCouponService.applyCoupon(customerId, cartId, applyCouponRequest);
        return ResponseEntity.ok(cartResponseMapper.toCartResponse(cartDTO));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<CartResponse> removeCouponFromCart(@PathVariable UUID cartId) {
        UUID customerId = securityService.getCurrentCustomerId();
        CartDTO cartDTO = cartCouponService.removeCouponFromCart(cartId, customerId);
        return ResponseEntity.ok(cartResponseMapper.toCartResponse(cartDTO));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeCouponFromCartItem(
            @PathVariable UUID cartId,
            @PathVariable UUID itemId
    ) {
        UUID customerId = securityService.getCurrentCustomerId();
        CartDTO cartDTO = cartCouponService.removeCouponFromCartItem(cartId, itemId, customerId);
        return ResponseEntity.ok(cartResponseMapper.toCartResponse(cartDTO));
    }

}
