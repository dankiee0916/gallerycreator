package com.gallery.gallerycreator.configs;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;
    public UserDetailsServiceImpl(UserRepository userRepo) { this.userRepo = userRepo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("User not found");

        // Role -> "ROLE_USER" etc.
        return org.springframework.security.core.userdetails.User
            .withUsername(u.getUsername())
            .password(u.getPassword())          
            .roles(u.getRole())                 
            .build();
    }
}
