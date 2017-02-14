package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import test_system.exception.AbstractRuntimeException;
import test_system.exception.NotFoundException;
import test_system.service.UserService;

@SuppressWarnings("unused")
@ControllerAdvice
public class RestControllerAdvice  extends AbstractController {

    @Autowired
    protected RestControllerAdvice(UserService userService) {
        super(userService);
    }

    @ExceptionHandler(Exception.class)
    final String handle(final Exception exception, final Model model) {
        exception.printStackTrace();

        if (exception instanceof AbstractRuntimeException || exception instanceof AccessDeniedException) {
            model.addAttribute("reason", exception.getMessage());
        }

        // todo: lets save template in class of exception by the custom annotations
        if (exception instanceof NotFoundException) {
            return run(Template.ERROR_404_TEMPLATE, model);
        }

        return run(Template.ERROR_TEMPLATE, model);
    }
}