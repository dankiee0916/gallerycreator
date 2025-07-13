package com.gallery.gallerycreator.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findByGallery(Gallery gallery);  
}
