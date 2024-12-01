package cz.jkdabing.backend.controller;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.FileConstants;
import cz.jkdabing.backend.dto.FileDTO;
import cz.jkdabing.backend.dto.response.MessageResponse;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.service.FileService;
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
@RequestMapping("/api/files")
public class FileController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    private final FileUploadProperties fileUploadProperties;

    public FileController(
            MessageService messageService,
            FileService fileService,
            FileUploadProperties fileUploadProperties
    ) {
        super(messageService);
        this.fileService = fileService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadFile(@Valid @ModelAttribute FileDTO fileDTO) {
        checkFile(fileDTO.getFile());

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

    private void checkFile(MultipartFile audioFile) {
        checkFileExtension(audioFile);
        checkAudioFileSize(audioFile);
    }

    private void checkFileExtension(MultipartFile file) {
        boolean isFileInvalid = !FileUtils.isFileValid(file.getOriginalFilename());
        if (isFileInvalid) {
            throw new BadRequestException(getLocalizedMessage("error.file.invalid.extension"));
        }
    }

    private void checkAudioFileSize(MultipartFile file) {
        boolean isFileTooLarge = file.getSize() > fileUploadProperties.getMaxCompressedFileSize();
        if (isFileTooLarge) {
            throw new BadRequestException(getLocalizedMessage("error.file.size.exceed.limit"));
        }
    }
}
