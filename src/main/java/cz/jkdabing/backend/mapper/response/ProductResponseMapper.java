package cz.jkdabing.backend.mapper.response;

import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.response.ProductAdminResponse;
import cz.jkdabing.backend.dto.response.ProductBasicResponse;
import cz.jkdabing.backend.dto.response.ProductCustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {

    @Named("toProductAdminResponse")
    ProductAdminResponse toProductAdminResponse(ProductDTO productDTO);

    List<ProductBasicResponse> toProductBasicResponseList(List<ProductDTO> productDTOList);

    ProductCustomerResponse toProductCustomerResponse(ProductDTO productDTO);
}
