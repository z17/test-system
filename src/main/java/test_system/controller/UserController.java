package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import test_system.data.UserData;
import test_system.service.UserService;

@Controller
public class UserController extends AbstractController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @RequestMapping(value = "/login")
    public String login(@RequestParam(name = "error", required = false) final Object error, final Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return run(Template.LOGIN_PAGE_TEMPLATE, model);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String users(final Model model) {
        model.addAttribute("users", userService.usersPage());
        model.addAttribute("user", new UserData());
        return run(Template.USERS_PAGE_TEMPLATE, model);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String usersPost(@ModelAttribute final UserData userData, final Model model) {
        userService.save(userData);
        return run(users(model), model);
    }
}
