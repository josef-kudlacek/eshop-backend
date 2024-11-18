package cz.jkdabing.backend.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FileStorageProperties {

    @Value("${file.upload-directory}")
    private String uploadDirectory;

}
