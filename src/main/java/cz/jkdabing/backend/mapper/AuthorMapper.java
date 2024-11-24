package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.AuthorDTO;
import cz.jkdabing.backend.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorEntity toEntity(AuthorDTO authorDTO);

    AuthorEntity updateFromDTO(AuthorDTO authorDTO, @MappingTarget AuthorEntity authorEntity);

    List<AuthorDTO> toDTOs(List<AuthorEntity> authorEntityList);
}
