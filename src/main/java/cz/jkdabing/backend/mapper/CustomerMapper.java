package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface CustomerMapper {

    CustomerEntity toEntity(CustomerDTO customerDTO);

    @Mapping(target = "customerId", ignore = true)
    void updateEntity(CustomerDTO customerDTO, @MappingTarget CustomerEntity customerEntity);
}
