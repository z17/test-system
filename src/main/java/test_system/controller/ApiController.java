package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test_system.data.WorkCreateData;
import test_system.service.WorkService;

@RestController
@RequestMapping(value = "/api")
public class ApiController {
    private final WorkService workService;

    @Autowired
    public ApiController(WorkService workService) {
        this.workService = workService;
    }

    @RequestMapping(value = "/updateWork", method = RequestMethod.POST)
    public boolean createWork(@RequestBody final WorkCreateData data) {
        workService.updateWork(data);
        return true;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/work/{id}/delete", method = RequestMethod.GET)
    public Boolean delete(@PathVariable final long id) {
        workService.delete(id);
        return true;
    }
}
