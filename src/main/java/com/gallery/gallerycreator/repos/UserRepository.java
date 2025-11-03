package com.gallery.gallerycreator.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gallery.gallerycreator.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
