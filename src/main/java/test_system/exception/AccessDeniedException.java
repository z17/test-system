package test_system.exception;

import org.springframework.http.HttpStatus;
import test_system.controller.Template;

@HttpCode(HttpStatus.FORBIDDEN)
@ErrorTemplate(Template.ERROR_TEMPLATE)
public class AccessDeniedException extends AbstractRuntimeException {
    public AccessDeniedException(String reason) {
        super(reason);
    }
}
