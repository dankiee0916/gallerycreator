package com.gallery.gallerycreator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show registration form
    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("session", session);
        return "register";
    }

    // Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user); // saves the hashed user
        return "redirect:/login";
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        model.addAttribute("session", session);
        return "login";
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
