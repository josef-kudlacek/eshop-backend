package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.dto.response.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private UUID productId;

    @NotBlank(message = "{error.product.name}")
    private String productName;

    @NotBlank(message = "{error.product.type}")
    private String productType;

    private ImageResponse image;

    private AudioFileResponse example;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal vat;

    private List<ProductAuthorResponse> authors;

    @NotBlank(message = "{error.product.description}")
    private String productDescription;

    private ZonedDateTime publishedDate;

    private ZonedDateTime withdrawalDate;

    private boolean isActive;

    private List<ProductGenreResponse> genres;

    private List<ProductFormatResponse> formats;
}
