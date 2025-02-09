package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.ResponseConstants;
import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.CartItemDTO;
import cz.jkdabing.backend.dto.request.UpdateCartItemQuantityRequest;
import cz.jkdabing.backend.dto.response.CartResponse;
import cz.jkdabing.backend.mapper.response.CartResponseMapper;
import cz.jkdabing.backend.security.config.SecurityConfig;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CartService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController extends AbstractBaseController {

    private final JwtTokenProvider jwtTokenProvider;

    private final CartService cartService;

    private final CartResponseMapper cartResponseMapper;

    private final SecurityService securityService;

    private final SecurityConfig securityConfig;

    public CartController(
            MessageService messageService,
            JwtTokenProvider jwtTokenProvider,
            CartService cartService,
            CartResponseMapper cartResponseMapper,
            SecurityService securityService,
            SecurityConfig securityConfig
    ) {
        super(messageService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.cartService = cartService;
        this.cartResponseMapper = cartResponseMapper;
        this.securityService = securityService;
        this.securityConfig = securityConfig;
    }

    @GetMapping
    public CartResponse getCart() {
        UUID customerId = securityService.getCurrentCustomerId();
        CartDTO cartDTO = cartService.getCart(customerId);
        return cartResponseMapper.toCartResponse(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @Valid @RequestBody CartItemDTO cartItemDTO, HttpServletResponse httpServletResponse
    ) {
        UUID customerId = securityService.getCurrentCustomerId();
        CartDTO cartDTO = cartService.addItemToCart(customerId, cartItemDTO);

        if (customerId == null) {
            String token = jwtTokenProvider.createCustomerToken(cartDTO.getCustomer().getCustomerId().toString());
            Cookie cookie = SecurityUtil.createCookie(ResponseConstants.COOKIE_ACCESS_TOKEN, token, securityConfig.getCustomerAccessTokenExpirationSeconds());
            httpServletResponse.addCookie(cookie);
        }

        CartResponse cartResponse = cartResponseMapper.toCartResponse(cartDTO);
        return ResponseEntity.ok()
                .body(cartResponse);
    }

    @DeleteMapping("/remove/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItemFromCart(@PathVariable String cartItemId) {
        UUID customerId = securityService.getCurrentCustomerId();
        cartService.removeItemFromCart(customerId, UUID.fromString(cartItemId));
    }

    @PatchMapping("/{cartId}/items/{cartItemId}")
    public void updateCartItemQuantity(
            @PathVariable UUID cartId,
            @PathVariable UUID cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityRequest request
    ) {
        UUID customerId = securityService.getCurrentCustomerId();
        cartService.updateCartItemQuantity(customerId, cartId, cartItemId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable UUID cartId) {
        UUID customerId = securityService.getCurrentCustomerId();
        cartService.clearCart(customerId, cartId);
    }

}
