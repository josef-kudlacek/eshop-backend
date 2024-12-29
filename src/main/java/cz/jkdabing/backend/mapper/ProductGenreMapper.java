package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductGenreDTO;
import cz.jkdabing.backend.dto.response.ProductGenreResponse;
import cz.jkdabing.backend.entity.ProductGenreEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductGenreMapper {

    ProductGenreEntity toEntity(ProductGenreDTO productGenreDTO);

    ProductGenreResponse toResponse(ProductGenreEntity productGenreEntity);
}
