package test_system.exception;

public class CustomRuntimeException extends AbstractRuntimeException {
    public CustomRuntimeException(String reason) {
        super(reason);
    }

    public CustomRuntimeException(String reason, Throwable e) {
        super(reason, e);
    }
}
