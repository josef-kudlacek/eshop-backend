package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.dto.ProductBaseDTO;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductController extends AbstractBaseController {

    private final ProductService productService;

    public ProductController(MessageService messageService, ProductService productService) {
        super(messageService);
        this.productService = productService;
    }

    @GetMapping
    public List<ProductBaseDTO> getProducts() {
        return productService.getActiveProducts();
    }

    //TODO: Ziskat detail produktu, kde nebude datum stazeni a zda je produkt aktivni (ProductSummaryDTO a ProductDetailDTO)
}
