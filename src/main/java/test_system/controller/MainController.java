package test_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test_system.data.UserData;
import test_system.entity.Role;
import test_system.service.TestService;
import test_system.service.TheoryService;
import test_system.service.UserService;
import test_system.service.WorkService;

@Controller
public class MainController {

    private final WorkService workService;

    private final TheoryService theoryService;

    private final TestService testService;

    private final UserService userService;

    @Autowired
    public MainController(WorkService workService, TheoryService theoryService, TestService testService, UserService userService) {
        this.workService = workService;
        this.theoryService = theoryService;
        this.testService = testService;
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(final Model model) {
        model.addAttribute("works", workService.getWorks());
        return Template.MAIN_TEMPLATE;
    }

    @RequestMapping(value = "/work/{id}", method = RequestMethod.GET)
    public String workPage(@PathVariable final long id, final Model model) {
        model.addAttribute("work", workService.getWork(id));
        return Template.WORK_PAGE_TEMPLATE;
    }

    @RequestMapping(value = "/work/{id}/theory", method = RequestMethod.GET)
    public String theoryPage(@PathVariable final long id, final Model model) {
        model.addAttribute("theory", theoryService.theoryPage(id));
        return Template.THEORY_PAGE_TEMPLATE;
    }

    @RequestMapping(value = "/work/{id}/test", method = RequestMethod.GET)
    public String testPage(@PathVariable final long id, final Model model) {
        model.addAttribute("test", testService.testPage(id));
        return Template.TEST_PAGE_TEMPLATE;
    }

    @RequestMapping(value = "/login")
    public String login(@RequestParam(name = "error", required = false) final Object error, final Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return Template.LOGIN_PAGE_TEMPLATE;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String users(final Model model) {
        model.addAttribute("users", userService.usersPage());
        model.addAttribute("user", new UserData());
        return Template.USERS_PAGE_TEMPLATE;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String usersPost(@ModelAttribute final UserData userData, final Model model) {
        userService.save(userData);
        return users(model);
    }
}