package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.request.ApplyCouponRequest;
import cz.jkdabing.backend.entity.CartEntity;
import cz.jkdabing.backend.entity.CartItemEntity;
import cz.jkdabing.backend.entity.CouponEntity;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.mapper.CartMapper;
import cz.jkdabing.backend.repository.CartItemRepository;
import cz.jkdabing.backend.repository.CartRepository;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartCouponServiceImpl extends AbstractService implements CartCouponService {

    private final CouponService couponService;

    private final CartService cartService;

    private final CartMapper cartMapper;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    protected CartCouponServiceImpl(
            MessageService messageService,
            AuditService auditService,
            CouponService couponService,
            CartService cartService,
            CartMapper cartMapper,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository
    ) {
        super(messageService, auditService);
        this.couponService = couponService;
        this.cartService = cartService;
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartDTO applyCoupon(UUID customerId, UUID cartId, ApplyCouponRequest applyCouponRequest) {
        CartEntity cartEntity = cartService.findCartByCartIdAndCustomerIdOrThrow(cartId, customerId);
        CouponEntity couponEntity = couponService.validateAndRetrieveCoupon(applyCouponRequest.getCouponCode());

        if (couponEntity.getApplicableProducts().isEmpty()) {
            applyCouponToCart(cartId, cartEntity, couponEntity);
        } else {
            applyCouponToCartItems(cartEntity, couponEntity);
        }

        return cartMapper.toDTO(cartEntity);
    }

    @Override
    public CartDTO removeCouponFromCart(UUID cartId, UUID customerId) {
        CartEntity cartEntity = cartService.findCartByCartIdAndCustomerIdOrThrow(cartId, customerId);
        if (cartEntity.getCoupon() == null) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.nothing.to.remove"));
        }

        cartEntity.setCoupon(null);
        cartRepository.save(cartEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartEntity.class),
                cartId,
                AuditLogConstants.COUPON_REMOVED
        );

        return cartMapper.toDTO(cartEntity);
    }

    @Override
    public CartDTO removeCouponFromCartItem(UUID cartId, UUID cartItemId, UUID customerId) {
        CartItemEntity cartItemEntity = cartItemRepository.findByCartItemIdAndCart_Customer_CustomerId(cartItemId, customerId)
                .orElseThrow(() -> new NotFoundException("error.cart.item.not.found"));
        if (cartItemEntity.getCoupon() == null) {
            throw new BadRequestException(getLocalizedMessage("error.coupon.nothing.to.remove"));
        }

        cartItemEntity.setCoupon(null);
        cartItemRepository.save(cartItemEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(CartItemEntity.class),
                cartItemId,
                AuditLogConstants.COUPON_REMOVED
        );

        return cartMapper.toDTO(cartItemEntity.getCart());
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
