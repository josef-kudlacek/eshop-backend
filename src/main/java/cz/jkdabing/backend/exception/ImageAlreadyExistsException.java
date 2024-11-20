package cz.jkdabing.backend.exception;

public class ImageAlreadyExistsException extends RuntimeException {

    public ImageAlreadyExistsException(String message) {
        super(message);
    }
}
