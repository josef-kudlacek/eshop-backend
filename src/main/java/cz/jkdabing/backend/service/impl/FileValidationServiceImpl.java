package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.enums.FileType;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.service.FileValidationService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileValidationServiceImpl implements FileValidationService {

    private static final String ERROR_PREFIX = "error.";

    private final MessageService messageService;

    private final FileUploadProperties fileUploadProperties;

    public FileValidationServiceImpl(MessageService messageService,
                                     FileUploadProperties fileUploadProperties
    ) {
        this.messageService = messageService;
        this.fileUploadProperties = fileUploadProperties;
    }


    @Override
    public void validateFile(MultipartFile file, FileType fileType) {
        checkFileIsEmpty(file, fileType);
        checkFileExtension(file, fileType);
        checkFileSize(file, fileType);
    }

    private void checkFileIsEmpty(MultipartFile file, FileType fileType) {
        boolean isFileEmpty = file == null || file.isEmpty();
        if (isFileEmpty) {
            String errorMessage = ERROR_PREFIX + fileType.toLowerCase() + ".file.empty";
            throw new BadRequestException(messageService.getMessage(errorMessage));
        }
    }

    private void checkFileExtension(MultipartFile file, FileType fileType) {
        boolean isFileInvalid = switch (fileType) {
            case AUDIO -> !FileUtils.isAudioFileValid(file.getOriginalFilename());
            case COMPRESSED -> !FileUtils.isCompressedFileValid(file.getOriginalFilename());
            default -> throw new BadRequestException(messageService.getMessage("error.file.type.not.supported"));
        };

        if (isFileInvalid) {
            String errorMessage = ERROR_PREFIX + fileType.toLowerCase() + ".file.invalid.extension";
            throw new BadRequestException(messageService.getMessage(errorMessage));
        }
    }

    private void checkFileSize(MultipartFile file, FileType fileType) {
        boolean isFileTooLarge = switch (fileType) {
            case AUDIO -> file.getSize() > fileUploadProperties.getMaxAudioFileSize();
            case COMPRESSED -> file.getSize() > fileUploadProperties.getMaxCompressedFileSize();
            default -> throw new BadRequestException(messageService.getMessage("error.file.type.not.supported"));
        };

        if (isFileTooLarge) {
            String errorMessage = ERROR_PREFIX + fileType.toLowerCase() + ".file.size.exceed.limit";
            throw new BadRequestException(messageService.getMessage(errorMessage));
        }
    }
}
