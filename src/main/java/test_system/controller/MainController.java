package test_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import test_system.service.MainService;

@Controller
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main(final Model model) {
        model.addAttribute("tasks", mainService.getTasks());
        return Template.MAIN_TEMPLATE;
    }
}
