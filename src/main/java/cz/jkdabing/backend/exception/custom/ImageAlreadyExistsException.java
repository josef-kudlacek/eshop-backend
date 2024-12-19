package cz.jkdabing.backend.exception.custom;

public class ImageAlreadyExistsException extends RuntimeException {

    public ImageAlreadyExistsException(String message) {
        super(message);
    }
}
