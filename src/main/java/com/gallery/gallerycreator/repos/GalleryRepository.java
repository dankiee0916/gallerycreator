package com.gallery.gallerycreator.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    // all galleries for a user
    List<Gallery> findByUser(User user);

    // get one gallery but also fetch the owner so we can check ownership
    @EntityGraph(attributePaths = { "user" })
    Optional<Gallery> findById(int id);

    // if you want gallery + owner + photos for the view page in one shot
    @EntityGraph(attributePaths = { "user", "photos" })
    Optional<Gallery> findWithUserAndPhotosById(int id);

    
}
