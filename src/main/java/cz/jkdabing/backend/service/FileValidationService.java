package cz.jkdabing.backend.service;

import cz.jkdabing.backend.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileValidationService {

    void validateFile(MultipartFile file, FileType fileType);
}
