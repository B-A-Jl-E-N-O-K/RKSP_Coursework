package com.example.rksp_coursework.services;


import com.example.rksp_coursework.models.*;
import com.example.rksp_coursework.repositories.BoughtSecuritiesRepository;
import com.example.rksp_coursework.repositories.SecSupRepository;
import com.example.rksp_coursework.repositories.SecuritiesRepository;
import com.example.rksp_coursework.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.rksp_coursework.controllers.AuthController.isReg;
import static com.example.rksp_coursework.controllers.AuthController.userId;


@Service
public class BuyService {
    private final SecuritiesRepository secRep;
    private final SecSupRepository secSupRep;

    private final UserRepository userRep;

    private final BoughtSecuritiesRepository buyRep;
    List<BinItem> itogList;



    public BuyService(SecSupRepository secSupRep, SecuritiesRepository secRep, UserRepository userRep, BoughtSecuritiesRepository buyRep) {
        this.secSupRep = secSupRep;
        this.secRep = secRep;
        this.userRep = userRep;
        this.buyRep = buyRep;
    }

    public int makeBuy(Securities last){
        if(!isReg) return -1; //не авторизован
        else{
            User currUser = userRep.findOneById(userId);
            double balance = currUser.getAccount();
            double sum = last.getPrice();
            if(balance < sum) return -2; //не хватает денег
            else{
                BoughtSec bought = new BoughtSec(last.getStrId(), last.getPrice(), currUser.getId());
                currUser.setAccount(balance - sum);
                userRep.save(currUser);
                buyRep.save(bought);
                return  1;
            }
        }


    }

    public void makeSell(int id){

        User currUser = userRep.findOneById(userId);
        for(int i=0; i<itogList.size(); i++){
            if (id == itogList.get(i).getId()){
                BinItem currSec = itogList.get(i);
                double balance = currUser.getAccount();
                double endPrice = currSec.getEndPrice();
                double sum = balance + endPrice;
                currUser.setAccount(sum);
                buyRep.deleteById(id);
                userRep.save(currUser);
            }
        }



    }



    public List<BinItem> showAllPortfolio(){
        List<BoughtSec> boughtList = buyRep.findAll();
        itogList = new ArrayList<>();
        for(int i = 0; i < boughtList.size(); i++){
            BoughtSec curr = boughtList.get(i);
            if(curr.getUserId() == userId){
                double startprice = curr.getStartPrice();
                BinItem currBin = new BinItem(curr.getId(), curr.getStrId(), startprice, curr.getUserId());
                List<Securities> secur = secRep.findAllByStrId(currBin.getStrId());
                Securities lastSecByName = secur.get(secur.size() - 1);
                double currPrice = lastSecByName.getPrice();
                currBin.setEndPrice(currPrice);
                double profitD = (currPrice - startprice) / startprice * 100;
                String profitS = String.format("%.2f",profitD) + "%";
                currBin.setProfit(profitS);
                itogList.add(currBin);
            }


        }

        return itogList;

    }





    /*




    public Iterable<Dish> getAll() {
        return dishRepository.findAll();
    }

    public Iterable<Dish> getFilteredMenu(String type) {
        Iterable<Dish> allDishes = dishRepository.findAll();
        List<Dish> dishesList = new ArrayList<>();
        for (Dish dish: allDishes){
            if(dish.getType().equals(TypeDish.valueOf(type))){
                dishesList.add(dish);
            }
        }
        return dishesList;
    }

    public Dish getOne(int id) {
        return dishRepository.findById(id).get();
    }

    public void save(Dish dish) {
        dishRepository.save(dish);
    }

    public String update(int id, Dish newDish){
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isEmpty()){
            return "Data not update.";
        }
        newDish.setId(id);
        dishRepository.save(newDish);
        return "Data update.";
    }

    public String delete(int id){
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isEmpty()){
            return "Data not found";
        }
        dishRepository.delete(optionalDish.get());
        return "Data delete.";
    }*/
}
