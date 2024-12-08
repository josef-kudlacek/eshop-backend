package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuthorMapper.class, ProductAuthorMapper.class, ProductFormatMapper.class, ProductGenreMapper.class,
                AudioFileMapper.class, ImageMapper.class
        }
)
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO productDTO);

    @Mapping(source = "productAuthorSet", target = "authors")
    @Mapping(source = "example", target = "example")
    @Mapping(source = "active", target = "isActive")
    ProductDTO toDTO(ProductEntity productEntity);

    ProductEntity updateEntity(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);

    List<ProductDTO> toDTOList(List<ProductEntity> productEntities);
}
