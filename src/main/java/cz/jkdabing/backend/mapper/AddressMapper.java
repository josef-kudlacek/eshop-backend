package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.AddressDTO;
import cz.jkdabing.backend.entity.AddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressEntity toEntity(AddressDTO addressDTO);
}
