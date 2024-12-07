package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductBaseDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.ProductDetailDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {
                AuthorMapper.class, ProductAuthorMapper.class, ProductFormatMapper.class, ProductGenreMapper.class,
                AudioFileMapper.class, ImageMapper.class
        }
)
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO productDTO);

    @Named("toDTO")
    ProductDTO toDTO(ProductEntity productEntity);

    @Mapping(source = "productAuthorSet", target = "authors")
    @Mapping(source = "example", target = "example")
    @Mapping(source = "active", target = "isActive")
    ProductDetailDTO toDetailDTO(ProductEntity productEntity);

    ProductEntity updateEntity(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);

    @Named("toBaseDTO")
    @Mapping(source = "productAuthorSet", target = "authors")
    @Mapping(target = "totalPrice", ignore = true)
    ProductBaseDTO toBaseDTO(ProductEntity productEntity);
}
