package com.gallery.gallerycreator.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gallery.gallerycreator.models.User;

// Basic repo to find users
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
