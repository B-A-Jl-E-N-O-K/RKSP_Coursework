package com.example.rksp_coursework.controllers;

import com.example.rksp_coursework.repositories.SecuritiesRepository;
import com.example.rksp_coursework.services.SecuritiesService;
import com.example.rksp_coursework.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secList")
public class SecuritiesController {
    private SecuritiesService secService;


    public SecuritiesController(SecuritiesService secService) {
        this.secService = secService;
    }

    @GetMapping()
    public String getCurrSecList()
    {

        return secService.getCurrSecJson();

    }

}
