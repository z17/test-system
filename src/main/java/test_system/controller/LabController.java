package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import test_system.service.LabService;
import test_system.service.UserService;

@Controller
public class LabController extends AbstractController  {
    private final LabService labService;

    @Autowired
    protected LabController(UserService userService, LabService labService) {
        super(userService);
        this.labService = labService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/lab", method = RequestMethod.GET)
    public String labPage(@PathVariable final long id, final Model model) {
        final String labTemplate = labService.labPage(id);
        return run(labTemplate, model);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/work/{id}/labComplete", method = RequestMethod.POST)
    public String labComplete(@PathVariable final long id, @RequestParam final Object data) {
        labService.finishLab(id, data);
        return "redirect:/work/" + id + "/finish";
    }
}
