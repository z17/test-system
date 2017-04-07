package test_system.exception;

import org.springframework.http.HttpStatus;
import test_system.controller.Template;

@HttpCode(HttpStatus.FORBIDDEN)
@ErrorTemplate(Template.ERROR_WORK_ACCESS_TEMPLATE)
public class WorkAlreadyExistsException extends AbstractRuntimeException {
}
