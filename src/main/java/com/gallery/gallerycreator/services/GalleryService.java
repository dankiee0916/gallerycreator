package com.gallery.gallerycreator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.GalleryRepository;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepo;

    // Get all galleries owned by a user
    public List<Gallery> getGalleriesByUser(User user) {
        return galleryRepo.findByUser(user);
    }

    // Get a gallery by ID
    public Optional<Gallery> getGalleryById(int id) {
        return galleryRepo.findById(id);
    }

    // Save or update a gallery
    public void saveGallery(Gallery gallery) {
        galleryRepo.save(gallery);
    }

    // Delete a gallery
    public void deleteGallery(int id) {
        galleryRepo.deleteById(id);
    }
}