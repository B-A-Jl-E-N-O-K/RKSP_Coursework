package com.example.rksp_coursework.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "sec_supply")
@NoArgsConstructor
public class SecSupply {
    public SecSupply(String datetime){

        this.datetime = datetime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable=false, updatable=false)
    private int id;

    @Column(name = "datetime")
    private String datetime;


}
