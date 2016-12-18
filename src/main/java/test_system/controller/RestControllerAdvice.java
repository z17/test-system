package test_system.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SuppressWarnings("unused")
@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(Exception.class)
    final String handle(final Exception exception) {
        exception.printStackTrace();
        return Template.ERROR_TEMPLATE;
    }
}