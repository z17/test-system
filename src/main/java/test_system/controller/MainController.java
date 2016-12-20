package test_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test_system.service.TestService;
import test_system.service.TheoryService;
import test_system.service.UserService;
import test_system.service.WorkService;

@Controller
public class MainController extends AbstractController {

    private final WorkService workService;

    private final TheoryService theoryService;

    private final TestService testService;

    @Autowired
    public MainController(WorkService workService, TheoryService theoryService, TestService testService, UserService userService) {
        super(userService);
        this.workService = workService;
        this.theoryService = theoryService;
        this.testService = testService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(final Model model) {
        model.addAttribute("works", workService.getWorks());
        return run(Template.MAIN_TEMPLATE, model);
    }

    @RequestMapping(value = "/work/{id}", method = RequestMethod.GET)
    public String workPage(@PathVariable final long id, final Model model) {
        model.addAttribute("work", workService.getWork(id));
        return run(Template.WORK_PAGE_TEMPLATE, model);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/theory", method = RequestMethod.GET)
    public String theoryPage(@PathVariable final long id, final Model model) {
        model.addAttribute("theory", theoryService.theoryPage(id));
        return run(Template.THEORY_PAGE_TEMPLATE, model);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/test", method = RequestMethod.GET)
    public String testPage(@PathVariable final long id, final Model model) {
        model.addAttribute("test", testService.testPage(id));
        return run(Template.TEST_PAGE_TEMPLATE, model);
    }
}