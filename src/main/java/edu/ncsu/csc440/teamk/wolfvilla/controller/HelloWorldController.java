package edu.ncsu.csc440.teamk.wolfvilla.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/helloworld")
public class HelloWorldController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getName(@RequestParam(value = "name", required = false, defaultValue = "Random Guy") String name) {
        ModelAndView mv = new ModelAndView("helloworld");
        mv.addObject("message", "Welcome to WolfVilla");
        mv.addObject("name", name);
        return mv;
    }
}
