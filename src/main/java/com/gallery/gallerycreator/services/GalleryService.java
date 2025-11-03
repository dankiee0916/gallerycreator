package com.gallery.gallerycreator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.GalleryRepository;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepo;

    // get all galleries owned by a user
    @Transactional(readOnly = true)
    public List<Gallery> getGalleriesByUser(User user) {
        return galleryRepo.findByUser(user);
    }

    // Get ALL galleries (public view)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public java.util.List<Gallery> getAllGalleries() {
        return galleryRepo.findAll();
    }

    // get a gallery by id and fetch the owner (so calling code can safely read user.username)
    @Transactional(readOnly = true)
    public Optional<Gallery> getGalleryById(int id) {
        return galleryRepo.findById(id);
    }

    // sometimes we also want photos loaded for the view page
    @Transactional(readOnly = true)
    public Optional<Gallery> getGalleryWithUserAndPhotos(int id) {
        return galleryRepo.findWithUserAndPhotosById(id);
    }

    // save or update
    @Transactional
    public void saveGallery(Gallery gallery) {
        galleryRepo.save(gallery);
    }

    // delete
    @Transactional
    public void deleteGallery(int id) {
        galleryRepo.deleteById(id);
    }
}
