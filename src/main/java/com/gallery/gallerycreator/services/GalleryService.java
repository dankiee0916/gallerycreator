package com.gallery.gallerycreator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.GalleryRepository;

import io.micrometer.common.lang.NonNull;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepo;

    // Get all galleries owned by a user
    @Transactional(readOnly = true)
    public List<Gallery> getGalleriesByUser(User user) {
        return galleryRepo.findByUser(user);
    }

    // Get a gallery by ID
    @Transactional(readOnly = true)
    public Optional<Gallery> getGalleryById(int id) {
        return galleryRepo.findById(id);
    }

    // Save or update a gallery
    @Transactional
    public @NonNull void saveGallery(Gallery gallery) {
        galleryRepo.save(gallery);
    }

    // Delete a gallery
    @Transactional
    public void deleteGallery(int id) {
        galleryRepo.deleteById(id);
    }
}
