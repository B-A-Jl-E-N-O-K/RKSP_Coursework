package com.example.rksp_coursework.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "securities")
@NoArgsConstructor
public class Securities {

    public Securities(int supId, String strId, String name, double price){
        this.supId = supId;
        this.strId = strId;
        this.name = name;
        this.price = price;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable=false, updatable=false)
    private int id;

    @Column(name = "supId")
    private int supId;

    @Column(name = "strId")
    private String strId;
    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

}
