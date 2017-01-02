package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(value = "/createWork", method = RequestMethod.POST)
    public boolean createWork(@RequestBody final WorkCreateData data) {
        workService.createWork(data);
        return true;
    }
}
