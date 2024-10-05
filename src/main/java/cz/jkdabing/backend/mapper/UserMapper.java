package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserDTO userDTO);
}
