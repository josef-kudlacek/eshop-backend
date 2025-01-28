package cz.jkdabing.backend.exception.custom;

public class FileNameAlreadyExistsException extends RuntimeException {

    public FileNameAlreadyExistsException(String message) {
        super(message);
    }
}
