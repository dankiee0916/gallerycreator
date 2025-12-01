package com.gallery.gallerycreator.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    // photos for one gallery
    List<Photo> findByGallery(Gallery gallery);

    // photo with gallery + gallery.user loaded (used for edit/delete)
    @EntityGraph(attributePaths = { "gallery", "gallery.user" })
    Optional<Photo> findById(Integer id);

    // gets the first photo for a gallery (smallest id)
    Photo findFirstByGalleryOrderByIdAsc(Gallery gallery);


}
