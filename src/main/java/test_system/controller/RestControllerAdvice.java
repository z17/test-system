package test_system.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import test_system.exception.NotFoundException;

@SuppressWarnings("unused")
@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(Exception.class)
    final String handle(final Exception exception, final Model model) {
        exception.printStackTrace();

        model.addAttribute("reason", exception.getMessage());

        // todo: lets save template in class of exception by the custom annotations
        if (exception instanceof NotFoundException) {
            return Template.ERROR_404_TEMPLATE;
        }

        return Template.ERROR_TEMPLATE;
    }
}