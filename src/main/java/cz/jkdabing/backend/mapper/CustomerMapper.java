package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerEntity toEntity(CustomerDTO customerDTO);
}
