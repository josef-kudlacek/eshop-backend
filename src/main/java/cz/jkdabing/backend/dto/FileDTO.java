package cz.jkdabing.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
public class FileDTO {

    @NotNull
    private UUID productId;

    @NotNull
    private MultipartFile file;

}
