package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO productDTO);

    ProductDTO toDTO(ProductEntity productEntity);

    ProductEntity updateEntity(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);
}
