package cz.jkdabing.backend.mapper.response;

import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.dto.response.CartResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartResponseMapper {

    CartResponse toCartResponse(CartDTO cartDTO);
}
