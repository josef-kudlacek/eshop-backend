package cz.jkdabing.backend.mapper.response;

import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.response.CartResponse;
import cz.jkdabing.backend.mapper.CouponMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CouponMapper.class})
public interface CartResponseMapper {

    CartResponse toCartResponse(CartDTO cartDTO);
}
