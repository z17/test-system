package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

    @Autowired
    public TestController(UserService userService, TestService testService) {
        super(userService);
        this.testService = testService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/test", method = RequestMethod.GET)
    public String testPage(@PathVariable final long id, final Model model) {
        model.addAttribute("test", testService.testPage(id));
        return run(Template.TEST_PAGE_TEMPLATE, model);
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/testComplete", method = RequestMethod.POST)
    public String testComplete(@PathVariable final long id, @RequestParam final MultiValueMap<String, String> data) {
        return "redirect:/" + testService.finishTest(id, data);
    }
}
