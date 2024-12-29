package cz.jkdabing.backend.exception.custom;

public class ExampleAlreadyExistsException extends RuntimeException {

    public ExampleAlreadyExistsException(String message) {
        super(message);
    }
}
