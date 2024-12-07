package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDTO extends ProductBaseDTO {

    @NotBlank(message = "{error.product.description}")
    private String productDescription;

    private ZonedDateTime publishedDate;

    private ZonedDateTime withdrawalDate;

    private boolean isActive;

}
