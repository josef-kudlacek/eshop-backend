package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.config.FileUploadProperties;
import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.AudioFileDTO;
import cz.jkdabing.backend.entity.AudioFileEntity;
import cz.jkdabing.backend.entity.ProductEntity;
import cz.jkdabing.backend.exception.custom.AudioFileNotExistException;
import cz.jkdabing.backend.exception.custom.ExampleAlreadyExistsException;
import cz.jkdabing.backend.exception.custom.FileNameAlreadyExistsException;
import cz.jkdabing.backend.mapper.AudioFileMapper;
import cz.jkdabing.backend.repository.AudioFileRepository;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AudioFileServiceImpl extends AbstractService implements AudioFileService {

    private static final Logger logger = LoggerFactory.getLogger(AudioFileServiceImpl.class);

    private final AudioFileRepository audioFileRepository;

    private final AudioFileMapper audioFileMapper;

    private final ProductService productService;

    private final FileUploadProperties fileUploadProperties;

    public AudioFileServiceImpl(
            MessageService messageService,
            AuditService auditService,
            AudioFileRepository audioFileRepository,
            AudioFileMapper audioFileMapper,
            ProductService productService,
            FileUploadProperties fileUploadProperties
    ) {
        super(messageService, auditService);
        this.audioFileRepository = audioFileRepository;
        this.audioFileMapper = audioFileMapper;
        this.productService = productService;
        this.fileUploadProperties = fileUploadProperties;
    }

    @Transactional
    @Override
    public void delete(String audioFileId) {
        UUID internalId = UUID.fromString(audioFileId);
        AudioFileEntity audioFileEntity = audioFileRepository.findById(internalId)
                .orElseThrow(() -> new AudioFileNotExistException(
                        getLocalizedMessage("error.audio.file.not.exists", audioFileId)
                ));

        boolean isSample = audioFileEntity.isSample();
        if (isSample && audioFileEntity.getProduct() != null) {
            ProductEntity productEntity = audioFileEntity.getProduct();
            productEntity.setExample(null);
            productService.updateProduct(productEntity);
        }

        deleteAudioFile(isSample, audioFileEntity.getFileUrl(), audioFileEntity.getFileName());

        audioFileRepository.delete(audioFileEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(AudioFileEntity.class),
                internalId,
                AuditLogConstants.ACTION_DELETE
        );
    }

    @Override
    public void saveAudioFile(@Valid AudioFileDTO audioFileDTO, @NotEmpty String audioFilePath) throws IOException {
        String audioFileName = audioFileDTO.getAudioFile().getOriginalFilename();
        if (audioFileRepository.existsByFileName(audioFileName)) {
            String fileNameAlreadyExistsMessage =
                    getLocalizedMessage("error.audio.file.name.already.exists", audioFileName);
            throw new FileNameAlreadyExistsException(fileNameAlreadyExistsMessage);
        }

        ProductEntity productEntity = productService.findProductByIdOrThrow(audioFileDTO.getProductId());
        if (audioFileDTO.isSample() && productEntity.getExample() != null) {
            String productAlreadyHasExampleMessage =
                    getLocalizedMessage("error.product.has.example", productEntity.getProductName());
            throw new ExampleAlreadyExistsException(productAlreadyHasExampleMessage);
        }

        uploadAndSaveAudioFile(productEntity, audioFileDTO, audioFilePath);
    }

    private void uploadAndSaveAudioFile(
            ProductEntity productEntity,
            @Valid AudioFileDTO audioFileDTO,
            @NotEmpty String audioFilePath
    ) throws IOException {
        AudioFileEntity audioFileEntity = audioFileMapper.prepareEntity(audioFileDTO);
        uploadAudioFile(audioFileDTO, audioFilePath, audioFileEntity.getFileName());

        audioFileEntity.setFileUrl(audioFilePath);
        audioFileEntity.setProduct(productEntity);
        audioFileRepository.save(audioFileEntity);

        if (audioFileDTO.isSample()) {
            productEntity.setExample(audioFileEntity);
            productService.updateProduct(productEntity);
        }

        prepareAuditLog(
                TableNameUtil.getTableName(audioFileEntity.getClass()),
                audioFileEntity.getAudioFileId(),
                AuditLogConstants.ACTION_UPLOAD
        );
    }

    private void uploadAudioFile(
            @Valid AudioFileDTO audioFileDTO,
            @NotEmpty String filePath,
            @NotEmpty String fileName
    ) throws IOException {
        String uploadDirectoryPath = getAudioFileDirectory(audioFileDTO.isSample());

        Path uploadPath = Paths.get(uploadDirectoryPath + filePath, fileName);

        MultipartFile audioFile = audioFileDTO.getAudioFile();
        Files.write(uploadPath, audioFile.getBytes());
    }

    private void deleteAudioFile(boolean isSample, @NotEmpty String filePath, @NotEmpty String fileName) {
        String directoryPath = getAudioFileDirectory(isSample);
        Path path = Paths.get(directoryPath + filePath, fileName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException ioException) {
            logger.warn("File cannot be deleted: ", ioException);
        }
    }

    private String getAudioFileDirectory(boolean isSample) {
        return isSample ? fileUploadProperties.getUploadPublicDirectory()
                : fileUploadProperties.getUploadPrivateDirectory();
    }

}
