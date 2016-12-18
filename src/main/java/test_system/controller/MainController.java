package test_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test_system.service.TheoryService;
import test_system.service.WorkService;

@Controller
public class MainController {

    private final WorkService workService;

    private final TheoryService theoryService;

    @Autowired
    public MainController(WorkService workService, TheoryService theoryService) {
        this.workService = workService;
        this.theoryService = theoryService;
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
}