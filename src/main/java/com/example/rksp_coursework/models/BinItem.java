package com.example.rksp_coursework.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BinItem {  //Объекты класса сопоставляются хранимым в портфеле, но расширяют его для передачи на вывод

    public BinItem(int id, String strId, double startPrice, int userId){
        this.id = id;
        this.strId = strId;
        this.startPrice = startPrice;
        this.userId = userId;
    }

    private int id;
    private String strId;

    private double startPrice;

    private int userId;

    private double endPrice;

    private String profit;

}
