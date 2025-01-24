package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
public class AudioFileDTO {

    @NotNull
    private UUID productId;

    @NotNull
    private MultipartFile audioFile;

    private Long length;

    private Integer bitrate;

    private Integer trackNumber;

    @Getter(AccessLevel.NONE)
    private boolean isSample;

    public boolean isSample() {
        return isSample;
    }
}
