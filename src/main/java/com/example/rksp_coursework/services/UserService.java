package com.example.rksp_coursework.services;

import com.example.rksp_coursework.models.User;
import com.example.rksp_coursework.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.rksp_coursework.controllers.AuthController.userId;

@Service
public class UserService {
    private final UserRepository clientRepository;


    public UserService(UserRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Iterable<User> getAll() {
        return clientRepository.findAll();
    }

    public User getOne(int id) {
        return clientRepository.findById(id).get();
    }

    public boolean[] login(String name, String pass) {
        Iterable<User> allUsers = clientRepository.findAll();
        for (User user: allUsers){
            if((user.getName().equals(name) || user.getEmail().equals(name)) && user.getPassword().equals(pass)){
                int currUserId = user.getId();
                userId = currUserId;
                return new boolean[] {true, user.getAdmin()};
            }

        }

        return new boolean[] {false, false};
    }

    public void save(User client) {
        clientRepository.save(client);
    }

    public String update(int id, User newClient){
        Optional<User> optionalTelephone = clientRepository.findById(id);
        if (optionalTelephone.isEmpty()){
            return "Data not update.";
        }
        newClient.setId(id);
        clientRepository.save(newClient);
        return "Data update.";
    }

    public String delete(int id){
        Optional<User> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()){
            return "Data no.";
        }
        clientRepository.delete(optionalClient.get());
        return "Data delete.";
    }
}
