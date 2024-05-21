package com.example.rksp_coursework.repositories;

import com.example.rksp_coursework.models.SecSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.List;

public interface SecSupRepository extends JpaRepository<SecSupply, Integer> {
    SecSupply findFirstByOrderByIdDesc();
    SecSupply findFirstByOrderById();
}
