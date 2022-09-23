package com.gustavo.gimnasio.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.gimnasio.entity.User;
import com.gustavo.gimnasio.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ArrayList<User> getUsers(){
        return (ArrayList<User>) userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long idUser){
        return userRepository.findById(idUser);
    }

    public void deleteUser(Long idUser){
        userRepository.deleteById(idUser);
    }
}
