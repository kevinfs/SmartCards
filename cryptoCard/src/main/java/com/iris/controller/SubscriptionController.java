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
public class SubscriptionController {

    @Autowired
    private Tools tools;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/subscribe")
    public String subscribe(Model model) {
        model.addAttribute("sel", tools.alea(1000));
        return "subscribe";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/subscribe")
    public @ResponseBody String subscribe(@RequestParam(value = "login") String login,
                     @RequestParam(value = "password") String password,
                     @RequestParam(value = "sel") String sel) {

        User user = new User();
        user.setLogin(login);
        user.setSel(sel);
        user.setPassword(password);
        userRepository.save(user);
        return "OK";
    }
}
