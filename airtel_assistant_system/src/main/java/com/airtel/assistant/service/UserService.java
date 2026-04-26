package com.airtel.assistant.service;

import com.airtel.assistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Added to make it a Spring-managed Service
public class UserService {

    @Autowired // Removed "new" and replaced with Autowired to enable dependency injection
    private UserRepository userRepository;

    public boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    public String getRole(String username) {
        return userRepository.getRole(username);
    }
}