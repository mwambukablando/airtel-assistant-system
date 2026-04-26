package com.airtel.assistant.service;

import com.airtel.assistant.repository.UserRepository;

public class UserService {

    private UserRepository userRepository = new UserRepository();

    public boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    public String getRole(String username) {
        return userRepository.getRole(username);
    }
}