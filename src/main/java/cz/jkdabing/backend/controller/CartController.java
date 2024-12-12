package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.CartItemDTO;
import cz.jkdabing.backend.dto.response.CartResponse;
import cz.jkdabing.backend.mapper.response.CartResponseMapper;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CartService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController extends AbstractBaseController {

    private final JwtTokenProvider jwtTokenProvider;

    private final CartService cartService;

    private final CartResponseMapper cartResponseMapper;

    public CartController(
            MessageService messageService,
            JwtTokenProvider jwtTokenProvider,
            CartService cartService,
            CartResponseMapper cartResponseMapper
    ) {
        super(messageService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.cartService = cartService;
        this.cartResponseMapper = cartResponseMapper;
    }

    @GetMapping
    public CartResponse getCart(@RequestHeader(value = "Authorization", required = false) String token) {
        UUID customerId = getCustomerId(token);
        CartDTO cartDTO = cartService.getCart(customerId);
        return cartResponseMapper.toCartResponse(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Valid @RequestBody CartItemDTO cartItemDTO
    ) {
        UUID customerId = getCustomerId(token);
        CartDTO cartDTO = cartService.addItemToCart(customerId, cartItemDTO);
        CartResponse cartResponse = cartResponseMapper.toCartResponse(cartDTO);

        if (token == null || customerId == null) {
            token = jwtTokenProvider.createCustomerToken(cartDTO.getCustomer().getCustomerId().toString());
        }

        return ResponseEntity.ok()
                .header(HttpHeaderConstants.AUTHORIZATION, JWTConstants.BEARER + token)
                .body(cartResponse);
    }

    private UUID getCustomerId(String token) {
        String extractedToken = SecurityUtil.extractToken(token);
        if (extractedToken == null) {
            return null;
        }
        return UUID.fromString(jwtTokenProvider.getSubjectIdFromToken(extractedToken));
    }
}
