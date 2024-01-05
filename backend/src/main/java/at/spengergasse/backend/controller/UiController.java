package at.spengergasse.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UiController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public ModelAndView redirect() {
        return new ModelAndView("forward:/");
    }
}
