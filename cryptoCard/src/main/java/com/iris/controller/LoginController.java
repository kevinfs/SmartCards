package com.iris.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String login(@RequestParam(value = "login") String login, Model model) {
        model.addAttribute("login", login);
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/password")
    public String password(@RequestParam(value = "password") String password, Model model) {
        model.addAttribute("password", password);
        return "password";
    }
}
