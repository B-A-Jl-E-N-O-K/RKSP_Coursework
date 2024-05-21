package com.example.rksp_coursework.repositories;

import com.example.rksp_coursework.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findOneById(int id);
}
