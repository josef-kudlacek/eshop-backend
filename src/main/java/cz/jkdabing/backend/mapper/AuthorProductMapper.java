package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.entity.AuthorProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {AuthorMapper.class}
)
public interface AuthorProductMapper {

    AuthorProductEntity toEntity(AuthorProductDTO authorProductDTO);


    @Mapping(source = "author.authorId", target = "authorId")
    @Mapping(source = "author.firstName", target = "firstName")
    @Mapping(source = "author.lastName", target = "lastName")
    AuthorDTO toAuthorDTO(AuthorProductEntity authorProductEntity);
}
