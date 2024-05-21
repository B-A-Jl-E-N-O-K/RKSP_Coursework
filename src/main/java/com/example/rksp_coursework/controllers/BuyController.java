package com.example.rksp_coursework.controllers;


import com.example.rksp_coursework.models.LogSec;
import com.example.rksp_coursework.models.SecSupply;
import com.example.rksp_coursework.models.Securities;
import com.example.rksp_coursework.models.User;
import com.example.rksp_coursework.services.BuyService;
import com.example.rksp_coursework.services.SecuritiesService;
import com.example.rksp_coursework.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import static com.example.rksp_coursework.controllers.AuthController.userId;


@Controller
@RequestMapping("/buy")
public class BuyController {

    private final SecuritiesService secService;

    private final BuyService buyService;

    private final UserService userService;

    private final SecPageController secPageControl;

    public BuyController(SecuritiesService secService, BuyService buyService, SecPageController secPageControl, UserService userService) {

        this.secService = secService;
        this.buyService = buyService;
        this.secPageControl = secPageControl;
        this.userService = userService;
    }

    @GetMapping()
    public String getFullPortfolio(Model model)
    {

        if(userId != -1){
            User currUser = userService.getOne(userId);
            double accountVal = currUser.getAccount();
            String accountStr = String.format("%.2f",accountVal) + "Р";
            model.addAttribute("currAccountBalance", accountStr);
        }
        else{
            model.addAttribute("currAccountBalance", 0);
        }

        model.addAttribute("seclist", buyService.showAllPortfolio());
        return "investPortfolio";

    }

    @GetMapping("/sell/{id}")
    public RedirectView sell(Model model, @PathVariable int id)
    {
        buyService.makeSell(id);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/buy");
        return redirectView;


        //return this.getFullPortfolio(model);

    }

    @PostMapping("/{strId}")
    public RedirectView buyThisSec(Model model, @PathVariable String strId)
    {
        List<Securities> namedSecList =  secService.getSecNamed(strId);
        Securities last = namedSecList.get(namedSecList.size() - 1);
        int buyResult = buyService.makeBuy(last);
        RedirectView redirectView = new RedirectView();
        if(buyResult == -1){
            redirectView.setUrl("/sec/" + strId);
            return redirectView;
        }
        else if(buyResult == -2){
            redirectView.setUrl("/sec/" + strId);
            return redirectView;
        }


        redirectView.setUrl("/buy");
        return redirectView;

    }

}
