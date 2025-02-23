package cz.jkdabing.backend.dto;

import cz.jkdabing.backend.dto.response.AudioFileResponse;
import cz.jkdabing.backend.dto.response.ImageResponse;
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

    private AudioFileResponse example;

    private ImageResponse image;

    private List<AuthorDTO> authors;

    private List<ProductGenreDTO> genres;

    private List<ProductFormatDTO> formats;
}
