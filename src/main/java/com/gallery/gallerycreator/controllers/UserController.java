package com.gallery.gallerycreator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // Handle login logic
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.getUserByUsername(username);

        if (user != null && userService.checkPassword(password, user.getPassword())) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/galleries"; // or wherever you want to go after login
        } else {
            model.addAttribute("error", "Invalid username or password.");
            model.addAttribute("session", session);
            return "login";
        }
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
