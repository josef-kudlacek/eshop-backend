package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.ProductFormatDTO;
import cz.jkdabing.backend.entity.ProductFormatEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductFormatMapper {

    ProductFormatEntity toEntity(ProductFormatDTO productFormatDTO);

    ProductFormatDTO toDTO(ProductFormatEntity productFormatEntity);
}
