package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import test_system.service.TestService;
import test_system.service.UserService;
import test_system.service.WorkService;

import java.util.List;
import java.util.Map;

@Controller
public class TestController extends AbstractController {
    private final TestService testService;
    private final WorkService workService;

    @Autowired
    public TestController(UserService userService, TestService testService, WorkService workService) {
        super(userService);
        this.testService = testService;
        this.workService = workService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/test", method = RequestMethod.GET)
    public String testPage(@PathVariable final long id, final Model model) {
        model.addAttribute("test", testService.testPage(id));
        return run(Template.TEST_PAGE_TEMPLATE, model);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/finish", method = RequestMethod.POST)
    public String sendTestAnswers(@PathVariable final long id, @RequestParam final MultiValueMap<String, String> data, final Model model) {
        model.addAttribute("result", testService.finishPage(id, data));
        return run(Template.TEST_RESULT_PAGE_TEMPLATE, model);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestParam(required = false) final Map<Object, List<Object>> data, final Model model) {
        model.addAttribute("works", workService.getWorks());
        return run(Template.MAIN_TEMPLATE, model);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test2(final Model model) {
        model.addAttribute("works", workService.getWorks());
        return run(Template.MAIN_TEMPLATE, model);
    }
}
