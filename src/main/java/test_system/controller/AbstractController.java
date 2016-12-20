package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import test_system.service.UserService;

@Controller
public abstract class AbstractController {

    private final UserService userService;

    @Autowired
    protected AbstractController(UserService userService) {
        this.userService = userService;
    }

    String run(final String template, final Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return template;
    }
}
