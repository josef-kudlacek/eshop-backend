package cz.jkdabing.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO extends ProductDTO {

    private List<AuthorDTO> authors;

    private List<ProductGenreDTO> genres;
}
