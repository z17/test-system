package test_system.exception;

public class AccessDeniedException extends AbstractRuntimeException {
    public AccessDeniedException(String reason) {
        super(reason);
    }
}
