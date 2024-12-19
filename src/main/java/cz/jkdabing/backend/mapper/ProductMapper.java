package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.ProductDetailDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {AuthorMapper.class, AuthorProductMapper.class}
)
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO productDTO);

    ProductDTO toDTO(ProductEntity productEntity);

    @Mapping(source = "authorProductSet", target = "authors")
    ProductDetailDTO toDetailDTO(ProductEntity productEntity);

    ProductEntity updateEntity(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);
}
