package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.CartItemDTO;
import cz.jkdabing.backend.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface CartItemMapper {

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.productType", target = "productType")
    @Mapping(source = "product.image", target = "image")
    CartItemDTO toDTO(CartItemEntity cartItemEntity);

    CartItemEntity toEntity(CartItemDTO cartItemDTO);

    List<CartItemDTO> toDTOs(List<CartItemEntity> cartItemEntities);
}
