package cz.jkdabing.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.jkdabing.backend.enums.CartStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartResponse {

    private UUID cartId;

    private CartStatusType statusType;

    private List<CartItemResponse> cartItems;

}
