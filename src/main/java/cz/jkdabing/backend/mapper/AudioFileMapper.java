package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.AudioFileDTO;
import cz.jkdabing.backend.dto.response.AudioFileResponse;
import cz.jkdabing.backend.entity.AudioFileEntity;

public interface AudioFileMapper {

    AudioFileEntity prepareEntity(AudioFileDTO audioFileDTO);

    AudioFileResponse toDTO(AudioFileEntity audioFileEntity);
}
