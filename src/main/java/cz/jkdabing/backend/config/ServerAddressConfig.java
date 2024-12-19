package cz.jkdabing.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ServerAddressConfig {

    @Value("${server.absolute.address}")
    private String serverAddress;
}
