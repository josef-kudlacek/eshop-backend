package cz.jkdabing.backend.exception.custom;

public class InvalidFileExtensionException extends RuntimeException {

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}
