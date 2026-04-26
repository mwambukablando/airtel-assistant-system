package com.airtel.assistant.controller;

import com.airtel.assistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/admin/users")
    public String manageUsers(Model model) {
        // Now getting the list directly from the repo
        model.addAttribute("users", userRepo.getAllUsersList());
        return "manage-users";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@RequestParam String firstName, 
                           @RequestParam String lastName, 
                           @RequestParam String username) {
        userRepo.createUser(firstName, lastName, username);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam String username) {
        userRepo.deleteUser(username);
        return "redirect:/admin/users";
    }
}