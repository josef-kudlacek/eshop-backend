package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.FileDTO;

import java.io.IOException;

public interface FileService {

    void saveFile(FileDTO fileDTO, String filePath) throws IOException;

    void delete(String fileId);
}
