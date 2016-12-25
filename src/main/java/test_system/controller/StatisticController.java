package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import test_system.service.StatisticService;
import test_system.service.UserService;
import test_system.service.WorkService;

@Controller
@Secured("ROLE_ADMIN")
public class StatisticController extends AbstractController {

    private final WorkService workService;
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(final UserService userService, WorkService workService, StatisticService statisticService) {
        super(userService);
        this.workService = workService;
        this.statisticService = statisticService;
    }

    @RequestMapping(value = "/statistic")
    public String statisticPage(final Model model) {
        model.addAttribute("works", workService.getWorks());
        return run(Template.STATISTIC_MAIN_TEMPLATE, model);
    }


    @RequestMapping(value = "/statistic/{id}")
    public String statisticWorkPage(@PathVariable final long id, final Model model) {
        model.addAttribute("statistic", statisticService.getWorkStatistic(id));
        return run(Template.STATISTIC_WORK_TEMPLATE, model);
    }
}
