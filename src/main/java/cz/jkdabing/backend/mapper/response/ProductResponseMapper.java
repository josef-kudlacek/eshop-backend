package cz.jkdabing.backend.mapper.response;

import cz.jkdabing.backend.dto.ProductBasicDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.response.ProductAdminDTO;
import cz.jkdabing.backend.dto.response.ProductCustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {

    @Named("toProductAdminResponse")
    ProductAdminDTO toProductAdminResponse(ProductDTO productDTO);

    List<ProductBasicDTO> toProductBasicResponseList(List<ProductDTO> productDTOList);

    ProductCustomerDTO toProductCustomerResponse(ProductDTO productDTO);
}
