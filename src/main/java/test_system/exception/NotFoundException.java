package test_system.exception;

public class NotFoundException extends AbstractRuntimeException {
    public NotFoundException(final String reason) {
        super(reason);
    }
}
