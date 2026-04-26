package com.airtel.assistant.controller;

import com.airtel.assistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/admin/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", loadUsersFromDb());
        return "manage-users";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@RequestParam String firstName, 
                           @RequestParam String lastName, 
                           @RequestParam String username) {
        
        userRepo.createUser(firstName, lastName, username);
        return "redirect:/admin/users"; // Refreshes page to show new user in table
    }
    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam String username) {
        userRepo.deleteUser(username);
        return "redirect:/admin/users";
    }
    private List<Map<String, String>> loadUsersFromDb() {
        List<Map<String, String>> list = new ArrayList<>();
        try (ResultSet rs = userRepo.getAllUsers()) {
            while (rs != null && rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("username", rs.getString("username"));
                user.put("fullName", rs.getString("full_name"));
                user.put("role", rs.getString("role"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}