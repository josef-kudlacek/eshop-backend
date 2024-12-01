package cz.jkdabing.backend.mapper.impl;

import cz.jkdabing.backend.dto.AudioFileDTO;
import cz.jkdabing.backend.dto.response.AudioFileResponse;
import cz.jkdabing.backend.entity.AudioFileEntity;
import cz.jkdabing.backend.enums.AudioFormatType;
import cz.jkdabing.backend.exception.custom.InvalidFileExtensionException;
import cz.jkdabing.backend.mapper.AudioFileMapper;
import cz.jkdabing.backend.util.FileUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AudioFileMapperImpl implements AudioFileMapper {

    public AudioFileEntity prepareEntity(@NotNull @Valid AudioFileDTO audioFileDTO) {
        MultipartFile audioFile = audioFileDTO.getAudioFile();
        String fileName = audioFile.getOriginalFilename();
        String fileFormat = FileUtils.getFileExtension(fileName);

        if (fileFormat == null) {
            throw new InvalidFileExtensionException("error.audio.file.invalid.extension");
        }

        AudioFormatType audioFormatType = AudioFormatType.valueOf(fileFormat.toUpperCase());
        return AudioFileEntity.builder()
                .fileName(fileName)
                .audioFormatType(audioFormatType)
                .size(audioFile.getSize())
                .length(audioFileDTO.getLength())
                .bitrate(audioFileDTO.getBitrate())
                .trackNumber(audioFileDTO.getTrackNumber())
                .isSample(audioFileDTO.isSample())
                .build();
    }

    public AudioFileResponse toDTO(@NotNull AudioFileEntity audioFileEntity) {
        String fileName = audioFileEntity.getFileName();
        String filePath = audioFileEntity.getFileUrl() + fileName;

        return AudioFileResponse.builder()
                .fileName(fileName)
                .filePath(filePath)
                .build();
    }
}
