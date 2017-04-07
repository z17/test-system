package test_system.exception;

import org.springframework.http.HttpStatus;
import test_system.controller.Template;

@HttpCode(HttpStatus.INTERNAL_SERVER_ERROR)
@ErrorTemplate(Template.ERROR_TEMPLATE)
public class CustomRuntimeException extends AbstractRuntimeException {
    public CustomRuntimeException(String reason) {
        super(reason);
    }

    public CustomRuntimeException(String reason, Throwable e) {
        super(reason, e);
    }
}
