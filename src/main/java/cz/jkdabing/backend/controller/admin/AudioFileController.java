package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.controller.AbstractBaseController;
import cz.jkdabing.backend.dto.AudioFileDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.enums.FileType;
import cz.jkdabing.backend.service.AudioFileService;
import cz.jkdabing.backend.service.FileValidationService;
import cz.jkdabing.backend.service.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/audioFiles")
public class AudioFileController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(AudioFileController.class);

    private final AudioFileService audioFileService;

    private final FileValidationService fileValidationService;

    public AudioFileController(
            MessageService messageService,
            AudioFileService audioFileService,
            FileValidationService fileValidationService
    ) {
        super(messageService);
        this.audioFileService = audioFileService;
        this.fileValidationService = fileValidationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadAudioFile(
            @Valid @ModelAttribute AudioFileDTO audioFileDTO
    ) {
        fileValidationService.validateFile(audioFileDTO.getAudioFile(), FileType.AUDIO);

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
}
