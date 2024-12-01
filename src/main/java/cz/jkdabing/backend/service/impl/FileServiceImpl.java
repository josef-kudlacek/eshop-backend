package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.FileDTO;
import cz.jkdabing.backend.entity.FileEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.FileNameAlreadyExistsException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.repository.FileRepository;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl extends AbstractService implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final ProductService productService;

    private final FileUploadProperties fileUploadProperties;

    public FileServiceImpl(
            MessageService messageService,
            AuditService auditService,
            FileRepository fileRepository,
            ProductService productService,
            FileUploadProperties fileUploadProperties
    ) {
        super(messageService, auditService);
        this.fileRepository = fileRepository;
        this.productService = productService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @Override
    public void saveFile(@NotNull @Valid FileDTO fileDTO, @NotEmpty String filePath) throws IOException {
        MultipartFile file = fileDTO.getFile();
        if (fileRepository.existsByFileName(file.getOriginalFilename())) {
            String fileNameAlreadyExistsMessage =
                    getLocalizedMessage("error.file.name.already.exists", file.getOriginalFilename());
            throw new FileNameAlreadyExistsException(fileNameAlreadyExistsMessage);
        }

        ProductEntity productEntity = productService.findProductByIdOrThrow(fileDTO.getProductId());
        uploadAndSaveFile(productEntity, fileDTO.getFile(), filePath);
    }

    @Override
    public void delete(String fileId) {
        UUID internalId = UUID.fromString(fileId);
        FileEntity fileEntity = fileRepository.findById(internalId)
                .orElseThrow(() -> new NotFoundException(getLocalizedMessage("error.file.not.found", internalId)));

        deleteFile(fileEntity.getFileUrl(), fileEntity.getFileName());
        fileRepository.delete(fileEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(FileEntity.class),
                internalId,
                AuditLogConstants.ACTION_DELETE
        );
    }

    private void uploadAndSaveFile(ProductEntity productEntity, MultipartFile file, String filePath) throws IOException {
        uploadFile(file, filePath);

        FileEntity fileEntity = prepareFileEntity(file, filePath);

        fileEntity.setProduct(productEntity);
        fileRepository.save(fileEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(fileEntity.getClass()),
                fileEntity.getFileId(),
                AuditLogConstants.ACTION_UPLOAD
        );
    }

    private void uploadFile(MultipartFile file, String filePath) throws IOException {
        String uploadDirectoryPath = getAudioFileDirectory();
        Path uploadPath = Paths.get(uploadDirectoryPath + filePath, file.getOriginalFilename());

        Files.write(uploadPath, file.getBytes());
    }

    private FileEntity prepareFileEntity(MultipartFile file, String filePath) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileUrl(filePath);
        fileEntity.setSize(file.getSize());
        return fileEntity;
    }

    private void deleteFile(String filePath, String fileName) {
        String directoryPath = getAudioFileDirectory();
        Path path = Paths.get(directoryPath + filePath, fileName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException ioException) {
            logger.warn("File cannot be deleted: ", ioException);
        }
    }

    private String getAudioFileDirectory() {
        return fileUploadProperties.getUploadPrivateDirectory();
    }
}
