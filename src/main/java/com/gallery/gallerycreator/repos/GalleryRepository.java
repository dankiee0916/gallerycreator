package com.gallery.gallerycreator.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;

// Lets us pull all galleries owned by a specific user
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
    List<Gallery> findByUser(User user);
}
