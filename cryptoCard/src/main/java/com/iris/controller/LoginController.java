package com.iris.controller;

import com.iris.model.User;
import com.iris.repository.UserRepository;
import com.iris.service.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private Tools tools;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/graine")
    public String graine() {
        return String.valueOf(tools.alea(1000));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public String step2(@RequestParam(value = "login") String login,
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
