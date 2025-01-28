package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.dto.ProductBasicDTO;
import cz.jkdabing.backend.dto.ProductDTO;
import cz.jkdabing.backend.dto.response.ProductCustomerDTO;
import cz.jkdabing.backend.mapper.response.ProductResponseMapper;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
public class ProductController extends AbstractBaseController {

    private final ProductService productService;

    private final ProductResponseMapper productResponseMapper;

    public ProductController(
            MessageService messageService,
            ProductService productService,
            ProductResponseMapper productResponseMapper
    ) {
        super(messageService);
        this.productService = productService;
        this.productResponseMapper = productResponseMapper;
    }

    @GetMapping
    public List<ProductBasicDTO> getBasicProducts() {
        List<ProductDTO> activeProducts = productService.getActiveProducts();
        return productResponseMapper.toProductBasicResponseList(activeProducts);
    }

    @GetMapping("/{productId}")
    public ProductCustomerDTO getProduct(@PathVariable UUID productId) {
        ProductDTO productDTO = productService.getProduct(productId);
        return productResponseMapper.toProductCustomerResponse(productDTO);
    }
}
