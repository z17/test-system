package test_system.exception;

/**
 * Parent class for all exceptions, which messages will be returned to users
 */
public abstract class AbstractRuntimeException extends RuntimeException {
    public AbstractRuntimeException(final String reason) {
        super(reason);
    }
}
