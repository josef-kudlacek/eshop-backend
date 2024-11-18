package cz.jkdabing.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void saveImage(String productId, MultipartFile image, String imagePath) throws IOException;
}
