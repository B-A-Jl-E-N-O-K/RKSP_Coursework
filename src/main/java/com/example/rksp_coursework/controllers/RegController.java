package com.example.rksp_coursework.controllers;

import com.example.rksp_coursework.models.User;
import com.example.rksp_coursework.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/reg")
public class RegController {

    private final UserService userService;
    private final String passAdmin = "root";
    private final String codeVip = "root";

    public RegController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getRegPage()
    {
        return "reg";

    }

    @PostMapping()
    public String regUser(String name, String email, String password, String code)
    {
        User user;
        if(password.equals(this.passAdmin)){
            user = new User(name, email, password, true, true, 1000);
        }
        else if(code.equals(codeVip)){
            user = new User(name, email, password, true, false, 1000);
        }
        else{
            user = new User(name, email, password, false, false, 1000);
        }

        this.userService.save(user);

        return "auth";

    }
}
