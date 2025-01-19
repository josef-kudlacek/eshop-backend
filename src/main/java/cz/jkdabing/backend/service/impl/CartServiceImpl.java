package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.CartItemDTO;
import cz.jkdabing.backend.dto.request.ApplyCouponRequest;
import cz.jkdabing.backend.entity.*;
import cz.jkdabing.backend.enums.CartStatusType;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.CartItemMapper;
import cz.jkdabing.backend.mapper.CartMapper;
import cz.jkdabing.backend.repository.CartItemRepository;
import cz.jkdabing.backend.repository.CartRepository;
import cz.jkdabing.backend.repository.CustomerRepository;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl extends AbstractService implements CartService {

    private static final List<CartStatusType> ACTIVE_CART_STATUSES = Arrays.asList(
            CartStatusType.ACTIVE, CartStatusType.GUEST
    );

    private final CartRepository cartRepository;

    private final CartItemMapper cartItemMapper;

    private final CartMapper cartMapper;

    private final CustomerRepository customerRepository;

    private final ProductService productService;

    private final CartItemRepository cartItemRepository;

    private final CouponService couponService;

    public CartServiceImpl(
            MessageService messageService,
            AuditService auditService,
            CartRepository cartRepository,
            CartItemMapper cartItemMapper,
            CartMapper cartMapper,
            CustomerRepository customerRepository,
            ProductService productService,
            CartItemRepository cartItemRepository,
            CouponService couponService
    ) {
        super(messageService, auditService);
        this.cartRepository = cartRepository;
        this.cartItemMapper = cartItemMapper;
        this.cartMapper = cartMapper;
        this.customerRepository = customerRepository;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
        this.couponService = couponService;
    }

    @Override
    public CartDTO addItemToCart(UUID customerId, CartItemDTO cartItemDTO) {
        ProductEntity productEntity = productService.findProductByIdOrThrow(cartItemDTO.getProductId());
        UUID cartId = cartItemDTO.getCartId();

        CartEntity cartEntity;
        cartEntity = createCartForCustomer(customerId, cartId, productEntity);

        CartItemEntity cartItemEntity = cartItemMapper.toEntity(cartItemDTO);
        cartItemEntity.setProduct(productEntity);
        cartItemEntity.setCart(cartEntity);

        cartEntity.addCartItem(cartItemEntity);

        cartItemRepository.save(cartItemEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartItemEntity.class),
                cartItemEntity.getCartItemId(),
                AuditLogConstants.ACTION_ADD
        );

        return cartMapper.toDTO(cartEntity);
    }

    @Override
    public CartDTO getCart(UUID customerId) {
        if (customerId == null) {
            return null;
        }

        CartEntity cartEntity = cartRepository.findByStatusTypeInAndCustomer_CustomerId(
                ACTIVE_CART_STATUSES, customerId
        ).orElse(null);

        return cartMapper.toDTO(cartEntity);
    }

    @Override
    public void removeItemFromCart(UUID customerId, UUID cartItemId) {
        if (customerId == null) {
            throw new BadRequestException(getLocalizedMessage("error.customer.not.found"));
        }

        CartItemEntity cartItemEntity = cartItemRepository.findByCartItemIdAndCart_Customer_CustomerId(cartItemId, customerId)
                .orElseThrow(() -> new NotFoundException(getLocalizedMessage("error.cart.item.not.found")));
        cartItemRepository.delete(cartItemEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartItemEntity.class),
                cartItemId,
                AuditLogConstants.ACTION_REMOVE
        );
    }

    @Override
    public void updateCartItemQuantity(UUID customerId, UUID cartId, UUID cartItemId, int quantity) {
        CartItemEntity cartItemEntity = cartItemRepository.findByCartItemIdAndCart_Customer_CustomerId(cartItemId, customerId)
                .orElseThrow(() -> new NotFoundException(getLocalizedMessage("error.cart.item.not.found")));

        cartItemEntity.setQuantity(quantity);
        cartItemRepository.save(cartItemEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartItemEntity.class),
                cartItemId,
                AuditLogConstants.ACTION_UPDATE
        );
    }

    @Override
    public void clearCart(UUID customerId, UUID cartId) {
        CartEntity cartEntity = cartRepository.findByCartIdAndStatusTypeAndCustomer_CustomerId(
                cartId, ACTIVE_CART_STATUSES, customerId
        ).orElseThrow(() -> new NotFoundException(getLocalizedMessage("error.cart.not.found", cartId)));

        cartItemRepository.deleteAll(cartEntity.getCartItems());
        cartRepository.delete(cartEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartItemEntity.class),
                cartEntity.getCartId(),
                AuditLogConstants.ACTION_DELETE
        );
    }

    @Override
    public CartDTO applyCoupon(UUID customerId, UUID cartId, ApplyCouponRequest applyCouponRequest) {
        CartEntity cartEntity = findCartByCartIdAndCustomerIdOrThrow(cartId, customerId);
        CouponEntity couponEntity = couponService.validateAndRetrieveCoupon(applyCouponRequest.getCouponCode());

        if (couponEntity.getApplicableProducts().isEmpty()) {
            applyCouponToCart(cartId, cartEntity, couponEntity);
        } else {
            applyCouponToCartItems(cartEntity, couponEntity);
        }

        return cartMapper.toDTO(cartEntity);
    }

    private CartEntity createCartForCustomer(UUID customerId, UUID cartId, ProductEntity productEntity) {
        CartEntity cartEntity;
        if (customerId == null) {
            CustomerEntity customerEntity = createGuestCustomer();
            cartEntity = createNewCartForGuest(customerEntity);
        } else {
            CustomerEntity customerEntity = customerRepository.findById(customerId)
                    .orElseThrow(() -> new NotFoundException(getLocalizedMessage("error.customer.not.found")));
            cartEntity = checkOrCreateCart(cartId, customerEntity, productEntity);
        }
        return cartEntity;
    }

    private CustomerEntity createGuestCustomer() {
        CustomerEntity customerEntity = CustomerEntity.builder()
                .lastName("Guest")
                .build();

        customerRepository.save(customerEntity);
        return customerEntity;
    }

    private CartEntity findCartByCartIdAndCustomerIdOrThrow(UUID cartId, UUID customerId) {
        return cartRepository.findByCartIdAndStatusTypeAndCustomer_CustomerId(
                        cartId, ACTIVE_CART_STATUSES, customerId
                )
                .orElseThrow(() -> new NotFoundException(getLocalizedMessage("error.cart.not.found", cartId)));
    }

    private CartEntity checkOrCreateCart(UUID cartId, CustomerEntity customerEntity, ProductEntity productEntity) {
        CartEntity cartEntity;
        boolean isCartIdSent = cartId != null;
        if (isCartIdSent) {
            cartEntity = findCartByCartIdAndCustomerIdOrThrow(cartId, customerEntity.getCustomerId());
            checkProductAlreadyInCart(cartEntity, productEntity);
        } else {
            cartEntity = cartRepository.findByStatusTypeInAndCustomer_CustomerId(
                    ACTIVE_CART_STATUSES, customerEntity.getCustomerId()
            ).orElse(null);

            boolean hasCustomerCartAlready = cartEntity != null;
            if (hasCustomerCartAlready) {
                checkProductAlreadyInCart(cartEntity, productEntity);
            } else {
                cartEntity = createCartForCustomer(customerEntity);
            }
        }

        return cartEntity;
    }

    private CartEntity createNewCartForGuest(CustomerEntity customerEntity) {
        CartEntity cartEntity = CartEntity.builder()
                .customer(customerEntity)
                .statusType(CartStatusType.GUEST)
                .build();

        cartRepository.save(cartEntity);
        return cartEntity;
    }

    private void checkProductAlreadyInCart(CartEntity cartEntity, ProductEntity productEntity) {
        boolean productAlreadyExists = cartEntity.getCartItems().stream()
                .anyMatch(cartItemEntity -> cartItemEntity.getProduct().equals(productEntity));
        if (productAlreadyExists) {
            throw new BadRequestException(getLocalizedMessage("error.cart.item.already.exists", productEntity.getProductName()));
        }
    }

    private CartEntity createCartForCustomer(CustomerEntity customerEntity) {
        CartEntity newCartEntity = CartEntity.builder()
                .customer(customerEntity)
                .statusType(CartStatusType.ACTIVE)
                .build();

        cartRepository.save(newCartEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartEntity.class),
                newCartEntity.getCartId(),
                AuditLogConstants.ACTION_CREATE
        );

        return newCartEntity;
    }

    private void applyCouponToCart(UUID cartId, CartEntity cartEntity, CouponEntity couponEntity) {
        if (couponEntity.equals(cartEntity.getCoupon())) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.already.applied", couponEntity.getCode()));
        }

        cartEntity.applyCoupon(couponEntity);
        cartRepository.save(cartEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartEntity.class),
                cartId,
                AuditLogConstants.COUPON_APPLIED
        );
    }

    private void applyCouponToCartItems(CartEntity cartEntity, CouponEntity couponEntity) {
        List<CartItemEntity> applicableCartItems = getApplicableCartItems(cartEntity, couponEntity);

        if (applicableCartItems.isEmpty()) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.not.applicable", couponEntity.getCode()));
        } else {
            applicableCartItems.forEach(cartItemEntity -> cartItemEntity.applyCoupon(couponEntity));
            cartItemRepository.saveAll(applicableCartItems);

            prepareAuditLogForCartItems(applicableCartItems);
        }
    }

    private static List<CartItemEntity> getApplicableCartItems(CartEntity cartEntity, CouponEntity couponEntity) {
        return cartEntity.getCartItems().stream()
                .filter(cartItemEntity -> couponEntity.getApplicableProducts().stream()
                        .anyMatch(productEntity -> cartItemEntity.getProduct().equals(productEntity)
                                && !couponEntity.equals(cartItemEntity.getCoupon())
                        )
                )
                .toList();
    }

    private void prepareAuditLogForCartItems(List<CartItemEntity> applicableCartItems) {
        List<UUID> cartItemIds = applicableCartItems.stream()
                .map(CartItemEntity::getCartItemId)
                .toList();

        prepareAuditLogs(
                TableNameUtil.getTableName(CartEntity.class),
                cartItemIds,
                AuditLogConstants.COUPON_APPLIED
        );
    }
}
