package com.example.rksp_coursework.controllers;


import com.example.rksp_coursework.models.LogSec;
import com.example.rksp_coursework.models.SecSupply;
import com.example.rksp_coursework.models.Securities;
import com.example.rksp_coursework.models.User;
import com.example.rksp_coursework.services.SecuritiesService;
import com.example.rksp_coursework.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static com.example.rksp_coursework.controllers.AuthController.userId;


@Controller
@RequestMapping("/sec")
public class SecPageController {

    private final SecuritiesService secService;
    private final UserService userService;

    public SecPageController(SecuritiesService secService, UserService userService) {
        this.secService = secService;
        this.userService = userService;
    }

    @GetMapping("/{strId}")
    public String getThisSecList(Model model, @PathVariable String strId)
    {
        List<Securities> namedSecList =  secService.getSecNamed(strId);
        List<SecSupply> SecSupList =  secService.getSupList();
        List<LogSec> FinalList =  new ArrayList<>();
        for(int i = 0; i < namedSecList.size(); i++){
            Securities curr = namedSecList.get(i);
            SecSupply currSup = SecSupList.get(i);
            if (curr.getSupId() == currSup.getId()){
                FinalList.add(new LogSec(curr.getId(), curr.getSupId(), curr.getStrId(), curr.getName(), curr.getPrice(), currSup.getDatetime()));
            }
        }
        model.addAttribute("seclist", FinalList);
        //model.addAttribute("secSupList", secService.getSupList());

        model.addAttribute("secImg", secService.getImgNamed(strId));
        model.addAttribute("secGrow", secService.getGrowPercent(strId));
        model.addAttribute("numGrow", secService.getGrowRate(strId));
        model.addAttribute("isRich", true);
        this.hideSystem(model);
        this.hideRegSystem(model);


        if(userId != -1){
            User currUser = userService.getOne(userId);
            double accountVal = currUser.getAccount();
            String accountStr = String.format("%.2f",accountVal) + "Р";
            model.addAttribute("currAccountBalance", accountStr);
        }
        else{
            model.addAttribute("currAccountBalance", 0);
        }

        return "secPage";

    }


    public void hideSystem(Model model) {

        model.addAttribute("isAdmin", AuthController.isAdmin);

    }

    public void hideRegSystem(Model model) {

        model.addAttribute("isReg", AuthController.isReg);

    }
}
