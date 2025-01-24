package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.controller.AbstractBaseController;
import cz.jkdabing.backend.dto.AudioFileDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.service.AudioFileService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.FileUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/audio-files")
public class AudioFileController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(AudioFileController.class);

    private final AudioFileService audioFileService;

    private final FileUploadProperties fileUploadProperties;

    public AudioFileController(
            MessageService messageService,
            AudioFileService audioFileService,
            FileUploadProperties fileUploadProperties
    ) {
        super(messageService);
        this.audioFileService = audioFileService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadAudioFile(
            @Valid @ModelAttribute AudioFileDTO audioFileDTO
    ) {
        checkAudioFile(audioFileDTO.getAudioFile());

        try {
            audioFileService.saveAudioFile(audioFileDTO, FileConstants.PRODUCT_AUDIO_FILE_RELATIVE_PATH);
        } catch (IOException exception) {
            logger.error("Audio file was not saved correctly", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.server.side")));
        }

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("audio.file.uploaded")));
    }

    @DeleteMapping("/{audioFileId}")
    public ResponseEntity<MessageResponse> deleteAudioFile(@PathVariable("audioFileId") String audioFileId) {
        audioFileService.delete(audioFileId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("audio.file.deleted")));
    }

    private void checkAudioFile(MultipartFile audioFile) {
        checkAudioFileIsEmpty(audioFile);
        checkAudioFileExtension(audioFile);
        checkAudioFileSize(audioFile);
    }

    private void checkAudioFileIsEmpty(MultipartFile audioFile) {
        boolean audioFileIsEmpty = audioFile == null || audioFile.isEmpty();
        if (audioFileIsEmpty) {
            throw new BadRequestException(getLocalizedMessage("error.audio.file.empty"));
        }
    }

    private void checkAudioFileExtension(MultipartFile audioFile) {
        boolean audioFileIsInvalid = !FileUtils.isAudioFileValid(audioFile.getOriginalFilename());
        if (audioFileIsInvalid) {
            throw new BadRequestException(getLocalizedMessage("error.audio.file.invalid.extension"));
        }
    }

    private void checkAudioFileSize(MultipartFile audioFile) {
        boolean audioFileTooLarge = audioFile.getSize() > fileUploadProperties.getMaxAudioFileSize();
        if (audioFileTooLarge) {
            throw new BadRequestException(getLocalizedMessage("error.audio.file.size.exceed.limit"));
        }
    }
}
