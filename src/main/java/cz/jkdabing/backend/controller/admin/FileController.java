package cz.jkdabing.backend.controller.admin;

import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.controller.AbstractBaseController;
import cz.jkdabing.backend.dto.FileDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.enums.FileType;
import cz.jkdabing.backend.service.FileService;
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
@RequestMapping("/api/admin/files")
public class FileController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    private final FileValidationService fileValidationService;

    public FileController(
            MessageService messageService,
            FileService fileService,
            FileValidationService fileValidationService
    ) {
        super(messageService);
        this.fileService = fileService;
        this.fileValidationService = fileValidationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadFile(@Valid @ModelAttribute FileDTO fileDTO) {
        fileValidationService.validateFile(fileDTO.getFile(), FileType.COMPRESSED);

        try {
            fileService.saveFile(fileDTO, FileConstants.PRODUCT_FILE_RELATIVE_PATH);
        } catch (IOException exception) {
            logger.error("File was not saved correctly", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(getLocalizedMessage("error.server.side")));
        }

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("file.uploaded")));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<MessageResponse> deleteFile(@PathVariable("fileId") String fileId) {
        fileService.delete(fileId);

        return ResponseEntity.ok(new MessageResponse(getLocalizedMessage("file.deleted")));
    }
}
