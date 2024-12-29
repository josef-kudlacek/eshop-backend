package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.AudioFileDTO;

import java.io.IOException;

public interface AudioFileService {

    void saveAudioFile(AudioFileDTO audioFileDTO, String audioFilePath) throws IOException;

    void delete(String audioFileId);
}
