package test_system.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String reason) {
        super(reason);
    }
}
