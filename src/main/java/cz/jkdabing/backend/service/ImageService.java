package cz.jkdabing.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {

    void saveImage(String productId, MultipartFile image, String imagePath) throws IOException;

    void updateImage(String productId, MultipartFile image, String imagePath) throws IOException;

    void deleteImage(String productId) throws FileNotFoundException;
}
