package com.gallery.gallerycreator.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    // galleries for one user, load the user object too
    @EntityGraph(attributePaths = { "user" })
    List<Gallery> findByUser(User user);

    // all galleries, with user loaded (used on /galleries/all)
    @EntityGraph(attributePaths = { "user" })
    List<Gallery> findAll();

    // single gallery with user and photos (used on /galleries/{id})
    @EntityGraph(attributePaths = { "user", "photos" })
    Optional<Gallery> findById(Integer id);
}
