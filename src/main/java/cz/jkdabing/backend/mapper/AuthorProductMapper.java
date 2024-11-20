package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.AuthorProductDTO;
import cz.jkdabing.backend.entity.AuthorProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorProductMapper {

    AuthorProductEntity toEntity(AuthorProductDTO authorProductDTO);
}
