package cz.jkdabing.backend.mapper.impl;

import cz.jkdabing.backend.dto.response.ImageResponse;
import cz.jkdabing.backend.entity.ImageEntity;
import cz.jkdabing.backend.mapper.ImageMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl implements ImageMapper {

    @Override
    public ImageResponse toResponse(@NotNull ImageEntity imageEntity) {
        String imagePath = imageEntity.getImageUrl() + imageEntity.getImageName();

        return ImageResponse.builder()
                .imageName(imageEntity.getImageName())
                .imagePath(imagePath)
                .build();
    }
}
