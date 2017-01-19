package test_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test_system.service.TheoryService;
import test_system.service.UserService;
import test_system.service.WorkService;

@Controller
public class WorkController extends AbstractController {

    private final WorkService workService;

    private final TheoryService theoryService;

    @Autowired
    public WorkController(WorkService workService, TheoryService theoryService, UserService userService) {
        super(userService);
        this.workService = workService;
        this.theoryService = theoryService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(final Model model) {
        model.addAttribute("works", workService.getWorks());
        return run(Template.MAIN_TEMPLATE, model);
    }

    @PreAuthorize("isAuthenticated()")
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/work/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable final long id, final Model model) {
        model.addAttribute("data", workService.getWorkData(id));
        return run(Template.EDIT_PAGE_TEMPLATE, model);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/work/create", method = RequestMethod.GET)
    public String create(final Model model) {
        return run(Template.WORK_CREATE_TEMPLATE, model);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/finish", method = RequestMethod.GET)
    public String finish(@PathVariable final long id, final Model model) {
        // todo: show attempt page instead finish page
        model.addAttribute("data", workService.finishPage(id));
        return run(Template.TEST_RESULT_PAGE_TEMPLATE, model);
    }
}