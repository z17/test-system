package test_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import test_system.service.GalleryService;
import test_system.service.UserService;

@Controller
@Secured("ROLE_ADMIN")
public class GalleryController extends AbstractController {
    private final GalleryService galleryService;

    @Autowired
    protected GalleryController(UserService userService, GalleryService galleryService) {
        super(userService);
        this.galleryService = galleryService;
    }


    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public String statisticPage(final Model model) {
        model.addAttribute("files", galleryService.getFiles());
        return run(Template.GALLERY_TEMPLATE, model);
    }

}
