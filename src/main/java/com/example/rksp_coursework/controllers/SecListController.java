package com.example.rksp_coursework.controllers;


import com.example.rksp_coursework.services.SecuritiesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class SecListController {

    private final SecuritiesService secService;

    public SecListController(SecuritiesService secService) {
        this.secService = secService;
    }

    @GetMapping()
    public String getFullList(Model model)
    {
        model.addAttribute("seclist", secService.getCurrSupply());

        this.hideSystem(model);
        this.hideRegSystem(model);
        return "secVisualList";

    }

    public void hideSystem(Model model) {

        model.addAttribute("isAdmin", AuthController.isAdmin);

    }

    public void hideRegSystem(Model model) {

        model.addAttribute("isReg", AuthController.isReg);

    }
}
