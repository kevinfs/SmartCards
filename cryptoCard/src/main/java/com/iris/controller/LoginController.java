package com.iris.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(@RequestParam(value = "name") String name, Model model) {
        model.addAttribute("login", name);
        return "login";
    }
}
