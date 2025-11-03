package com.gallery.gallerycreator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Saves a new user with a hashed password
    @org.springframework.transaction.annotation.Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        // simple log so you can see it in Render logs
        System.out.println("Saved user: " + user.getUsername());
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    // Checks the password of user
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

