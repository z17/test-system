package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import test_system.exception.AbstractRuntimeException;
import test_system.exception.ErrorTemplate;
import test_system.exception.HttpCode;
import test_system.service.UserService;

import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
@ControllerAdvice
public class RestControllerAdvice extends AbstractController {

    @Autowired
    protected RestControllerAdvice(UserService userService) {
        super(userService);
    }

    @ExceptionHandler(Exception.class)
    final String handle(final Exception exception, final Model model, HttpServletResponse response) {
        exception.printStackTrace();

        setupUser(model);

        if (exception.getClass().isAnnotationPresent(HttpCode.class)) {
            HttpStatus status = exception.getClass().getAnnotation(HttpCode.class).value();
            response.setStatus(status.value());
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        String template;
        if (exception.getClass().isAnnotationPresent(ErrorTemplate.class)) {
            template = exception.getClass().getAnnotation(ErrorTemplate.class).value();
        } else {
            template = Template.ERROR_TEMPLATE;
        }

        if (exception instanceof AbstractRuntimeException || exception instanceof AccessDeniedException) {
            if (exception.getMessage() != null) {
                model.addAttribute("reason", exception.getMessage());
            }
        }

        return run(template, model);
    }
}