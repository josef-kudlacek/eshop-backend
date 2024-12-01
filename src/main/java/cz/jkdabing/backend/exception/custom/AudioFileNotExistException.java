package cz.jkdabing.backend.exception.custom;

public class AudioFileNotExistException extends RuntimeException {

    public AudioFileNotExistException(String message) {
        super(message);
    }
}
