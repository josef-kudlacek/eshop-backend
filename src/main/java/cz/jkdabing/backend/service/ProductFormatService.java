package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.ProductFormatDTO;
import jakarta.validation.Valid;

public interface ProductFormatService {

    void addFormatToProduct(@Valid ProductFormatDTO productFormatDTO);

    void deleteFormatFromProduct(int formatId);
}
