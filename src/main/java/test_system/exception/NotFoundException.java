package test_system.exception;


import org.springframework.http.HttpStatus;
import test_system.controller.Template;

@HttpCode(HttpStatus.NOT_FOUND)
@ErrorTemplate(Template.ERROR_404_TEMPLATE)
public class NotFoundException extends AbstractRuntimeException {
    public NotFoundException(final String reason) {
        super(reason);
    }
}
