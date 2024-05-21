package com.example.rksp_coursework.repositories;

import com.example.rksp_coursework.models.Securities;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.List;

public interface SecuritiesRepository extends JpaRepository<Securities, Integer> {
    void deleteAllBySupId(int oldId);
    int countBySupId(int supId);

    List<Securities> findAllByStrId(String strId);

    List<Securities> findAllBySupId(int supId);

}
