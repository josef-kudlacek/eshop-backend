package cz.jkdabing.backend.dto.response;

import cz.jkdabing.backend.dto.ProductBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductCustomerDTO extends ProductBasicDTO {

    private AudioFileResponse example;

    private String productDescription;

    private ZonedDateTime publishedDate;

    private List<ProductGenreResponse> genres;

    private List<ProductFormatResponse> formats;
}
