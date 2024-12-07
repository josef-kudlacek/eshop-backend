package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductAuthorDTO;
import cz.jkdabing.backend.dto.response.ProductAuthorResponse;
import cz.jkdabing.backend.entity.ProductAuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {AuthorMapper.class}
)
public interface ProductAuthorMapper {

    ProductAuthorEntity toEntity(ProductAuthorDTO productAuthorDTO);

    @Mapping(source = "author.authorId", target = "authorId")
    @Mapping(source = "author.firstName", target = "firstName")
    @Mapping(source = "author.lastName", target = "lastName")
    ProductAuthorResponse toResponse(ProductAuthorEntity productAuthorEntity);
}
