package test_system.exception;

/**
 * Parent class for all exceptions, which messages will be returned to users
 */
public abstract class AbstractRuntimeException extends java.lang.RuntimeException {
    AbstractRuntimeException() {
        super();
    }

    AbstractRuntimeException(Throwable e) {
        super(e);
    }

    AbstractRuntimeException(final String reason) {
        super(reason);
    }

    AbstractRuntimeException(final String reason, Throwable e) {
        super(reason, e);
    }

}
