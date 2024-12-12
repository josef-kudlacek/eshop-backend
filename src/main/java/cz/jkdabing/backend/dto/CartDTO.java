package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.enums.CartStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private UUID cartId;

    private CustomerDTO customer;

    private CartStatusType statusType;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private List<CartItemDTO> cartItems;
}
