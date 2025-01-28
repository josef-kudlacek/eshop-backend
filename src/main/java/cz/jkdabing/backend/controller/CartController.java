package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.CartItemDTO;
import cz.jkdabing.backend.dto.request.UpdateCartItemQuantityRequest;
import cz.jkdabing.backend.dto.response.CartResponse;
import cz.jkdabing.backend.mapper.response.CartResponseMapper;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CartService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.SecurityService;
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

    public CartController(
            MessageService messageService,
            JwtTokenProvider jwtTokenProvider,
            CartService cartService,
            CartResponseMapper cartResponseMapper,
            SecurityService securityService
    ) {
        super(messageService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.cartService = cartService;
        this.cartResponseMapper = cartResponseMapper;
        this.securityService = securityService;
    }

    @GetMapping
    public CartResponse getCart(@RequestHeader(value = "Authorization", required = false) String token) {
        UUID customerId = securityService.getCustomerId(token);
        CartDTO cartDTO = cartService.getCart(customerId);
        return cartResponseMapper.toCartResponse(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Valid @RequestBody CartItemDTO cartItemDTO
    ) {
        UUID customerId = securityService.getCustomerId(token);
        CartDTO cartDTO = cartService.addItemToCart(customerId, cartItemDTO);
        CartResponse cartResponse = cartResponseMapper.toCartResponse(cartDTO);

        if (token == null || customerId == null) {
            token = JWTConstants.BEARER + jwtTokenProvider.createCustomerToken(cartDTO.getCustomer().getCustomerId().toString());
        }

        return ResponseEntity.ok()
                .header(HttpHeaderConstants.AUTHORIZATION, token)
                .body(cartResponse);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public void removeItemFromCart(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable String cartItemId
    ) {
        UUID customerId = securityService.getCustomerId(token);
        cartService.removeItemFromCart(customerId, UUID.fromString(cartItemId));
    }

    @PatchMapping("/{cartId}/items/{cartItemId}")
    public void updateCartItemQuantity(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable UUID cartId,
            @PathVariable UUID cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityRequest request
    ) {
        UUID customerId = securityService.getCustomerId(token);
        cartService.updateCartItemQuantity(customerId, cartId, cartItemId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestHeader(value = "Authorization") String token, @PathVariable UUID cartId) {
        UUID customerId = securityService.getCustomerId(token);
        cartService.clearCart(customerId, cartId);
    }

}
