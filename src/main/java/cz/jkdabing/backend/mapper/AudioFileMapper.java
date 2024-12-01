package cz.jkdabing.backend.mapper;

import cz.jkdabing.backend.dto.AudioFileDTO;
import cz.jkdabing.backend.entity.AudioFileEntity;
import cz.jkdabing.backend.enums.AudioFormatType;
import cz.jkdabing.backend.exception.custom.InvalidFileExtensionException;
import cz.jkdabing.backend.util.FileUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface AudioFileMapper {

    @Named("customMapping")
    default AudioFileEntity prepareEntity(AudioFileDTO audioFileDTO) {
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
}
