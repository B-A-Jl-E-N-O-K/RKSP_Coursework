package com.example.rksp_coursework.repositories;

import com.example.rksp_coursework.models.BoughtSec;
import com.example.rksp_coursework.models.Securities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoughtSecuritiesRepository extends JpaRepository<BoughtSec, Integer> {

    BoughtSec findOneById(int id);

}
