package com.gallery.gallerycreator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.services.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show the registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());  // bind empty User object
        return "register";  // maps to register.html
    }

    // Handle form submission
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user) {
        // Optional: log for debug
        System.out.println("Registering user: " + user.getUsername());

        userService.saveUser(user);  // save user (with password hashing)
        return "redirect:/login";   // go to login page after success
    }

    // Show the login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // maps to login.html
    }

    // Optional: Handle logout redirect (Spring Security usually auto-handles this)
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
