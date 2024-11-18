package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO productDTO);

    ProductDTO toDTO(ProductEntity productEntity);
}
