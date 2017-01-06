package com.iris.controller;

import com.iris.model.User;
import com.iris.repository.UserRepository;
import com.iris.service.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private Tools tools;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String step1(Model model) {
        model.addAttribute("graine", tools.alea(1000));
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/graine")
    @ResponseBody
    public String graine() {
        return String.valueOf(tools.alea(1000));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String step2(@RequestParam(value = "login") String login, @RequestParam(value = "graine") String graine, Model model) {

        User user = userRepository.findByLogin(login);

        if (user != null) {
            // il existe
            model.addAttribute("login", user.getLogin());
            model.addAttribute("sel", user.getSel());

        } else {
            // il n'existe pas
            model.addAttribute("login", login);
            model.addAttribute("sel", tools.alea(1000));
        }

        model.addAttribute("graine", graine);
        return "password";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/password")
    public
    @ResponseBody
    String step3(@RequestParam(value = "login") String login,
                 @RequestParam(value = "password") String password,
                 @RequestParam(value = "graine") String graine) {

        User user = userRepository.findByLogin(login);
        if (user == null) {
            return "KO";
        }

        String v1 = user.getPassword();
        String v2 = tools.md5(v1 + graine);

        if (password.equals(v2)) {
            return "OK";
        }

        return "KO";
    }
}
