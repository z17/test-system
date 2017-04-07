package test_system.exception;

import org.springframework.http.HttpStatus;
import test_system.controller.Template;

@HttpCode(HttpStatus.INTERNAL_SERVER_ERROR)
@ErrorTemplate(Template.ERROR_TEMPLATE)
public class RuntimeException extends AbstractRuntimeException {
    public RuntimeException(Throwable e) {
        super(e);
    }

    public RuntimeException(String reason) {
        super(reason);
    }

    public RuntimeException(String reason, Throwable e) {
        super(reason, e);
    }
}
