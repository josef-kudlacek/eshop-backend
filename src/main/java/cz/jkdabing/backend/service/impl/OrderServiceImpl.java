package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.constants.CustomerConstants;
import cz.jkdabing.backend.entity.AddressEntity;
import cz.jkdabing.backend.entity.CartEntity;
import cz.jkdabing.backend.entity.OrderEntity;
import cz.jkdabing.backend.entity.OrderItemEntity;
import cz.jkdabing.backend.enums.AddressType;
import cz.jkdabing.backend.enums.OrderStatusType;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.repository.OrderRepository;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final CartService cartService;

    private final OrderRepository orderRepository;

    protected OrderServiceImpl(
            MessageService messageService,
            AuditService auditService,
            CartService cartService,
            OrderRepository orderRepository) {
        super(messageService, auditService);
        this.cartService = cartService;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void createOrder(UUID customerId) {
        if (customerId == null) {
            throw new BadRequestException(getLocalizedMessage("error.customer.not.found"));
        }

        CartEntity cartEntity = cartService.findCartByCustomerIdOrThrow(customerId);
        if (CustomerConstants.GUEST_NAME.equals(cartEntity.getCustomer().getLastName())) {
            throw new BadRequestException(getLocalizedMessage("error.customer.without.billing.info"));
        }

        createOrderEntity(cartEntity);

        cartService.orderCart(cartEntity);
    }

    private void createOrderEntity(CartEntity cartEntity) {
        AddressEntity lastBillingAddress = findLastBillingAddress(cartEntity);

        OrderEntity newOrder = OrderEntity.builder()
                .customer(cartEntity.getCustomer())
                .billingAddress(lastBillingAddress)
                .statusType(OrderStatusType.WAITING_FOR_PAYMENT)
                .createdAt(ZonedDateTime.now())
                .email(cartEntity.getCustomer().getEmail())
                .coupon(cartEntity.getCoupon())
                .build();

        addOrderItemsToOrder(cartEntity, newOrder);

        orderRepository.save(newOrder);

        prepareAuditLog(
                TableNameUtil.getTableName(OrderEntity.class),
                newOrder.getOrderId(),
                AuditLogConstants.ACTION_CREATE
        );
    }

    private void addOrderItemsToOrder(CartEntity cartEntity, OrderEntity orderEntity) {
        if (cartEntity == null || cartEntity.getCartItems() == null || cartEntity.getCartItems().isEmpty()) {
            throw new BadRequestException(getLocalizedMessage("error.cart.is.invalid"));
        }

        List<OrderItemEntity> orderItemEntities = cartEntity.getCartItems().stream()
                .map(cartItemEntity -> OrderItemEntity.builder()
                        .order(orderEntity)
                        .product(cartItemEntity.getProduct())
                        .quantity(cartItemEntity.getQuantity())
                        .orderItemPrice(cartItemEntity.getProduct().getPrice())
                        .orderItemVat(cartItemEntity.getProduct().getVat())
                        .coupon(cartItemEntity.getCoupon())
                        .build())
                .toList();

        orderEntity.setOrderItems(orderItemEntities);
    }

    private AddressEntity findLastBillingAddress(CartEntity cartEntity) {
        return cartEntity.getCustomer().getAddresses().stream()
                .filter(addressEntity -> AddressType.BILLING.equals(addressEntity.getAddressType()))
                .max(Comparator.comparing(AddressEntity::getCreatedAt))
                .orElseThrow(() -> new BadRequestException(getLocalizedMessage("error.customer.without.billing.address")));
    }
}
