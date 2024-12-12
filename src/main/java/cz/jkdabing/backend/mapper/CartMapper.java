package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.CartDTO;
import cz.jkdabing.backend.entity.CartEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    CartDTO toDTO(CartEntity cartEntity);
}
