package com.example.rksp_coursework.controllers;

import com.example.rksp_coursework.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private final SecListController secListControl;

    static boolean isAdmin = false;
    public static boolean isReg = false;
    public static int userId = -1;



    public AuthController(UserService userService, SecListController secListControl) {
        this.userService = userService;
        this.secListControl = secListControl;
    }

    @GetMapping()
    public String getAuthPage()
    {
        return "auth";

    }

    @PostMapping()
    public String loginUser(String name, String password, Model model)
    {
        boolean result[] = this.userService.login(name, password);
        if(result[0]){
            isAdmin = result[1];
            isReg = true;
            return secListControl.getFullList(model);
        }
        else{
            model.addAttribute("message", "Логин или пароль не верны");
            return "auth";
        }


    }
}
