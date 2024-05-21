package com.example.rksp_coursework.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bought_securities")
@NoArgsConstructor
public class BoughtSec {

    public BoughtSec(String strId, double startPrice, int userId){
        this.strId = strId;
        this.startPrice = startPrice;
        this.userId = userId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable=false, updatable=false)
    private int id;

    @Column(name = "strId")
    private String strId;

    @Column(name = "startPrice")
    private double startPrice;

    @Column(name = "userId")
    private int userId;

}
