package com.example.rksp_coursework.controllers;

import com.example.rksp_coursework.models.User;
import com.example.rksp_coursework.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class  UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getFullStaff(Model model)
    {
        model.addAttribute("userlist", userService.getAll());
        return "users";

    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getOne(@PathVariable int id) {
        return userService.getOne(id);
    }

}
