package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.response.ImageResponse;
import cz.jkdabing.backend.entity.ImageEntity;

public interface ImageMapper {

    ImageResponse toResponse(ImageEntity imageEntity);
}
