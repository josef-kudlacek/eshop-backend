package cz.jkdabing.backend.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

@Configuration
public class FileUploadProperties {

    @Value("${file.upload.public.directory}")
    @Getter
    private String uploadPublicDirectory;

    @Value("${file.upload.private.directory}")
    @Getter
    private String uploadPrivateDirectory;

    @Value("${file.upload.image.max.size}")
    @DataSizeUnit(DataUnit.MEGABYTES)
    private DataSize maxImageFileSize;

    @Value("${file.upload.audio.max.size}")
    @DataSizeUnit(DataUnit.GIGABYTES)
    private DataSize maxAudioFileSize;

    @Value("${file.upload.compressed.max.size}")
    @DataSizeUnit(DataUnit.GIGABYTES)
    private DataSize maxCompressedFileSize;

    public long getMaxImageFileSize() {
        return maxImageFileSize.toBytes();
    }

    public long getMaxAudioFileSize() {
        return maxAudioFileSize.toBytes();
    }

    public long getMaxCompressedFileSize() {
        return maxCompressedFileSize.toBytes();
    }
}
