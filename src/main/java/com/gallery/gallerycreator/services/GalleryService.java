package com.gallery.gallerycreator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.Photo;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.GalleryRepository;
import com.gallery.gallerycreator.repos.PhotoRepository;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private PhotoRepository photoRepository;

    // list galleries for one user
    @Transactional(readOnly = true)
    public List<Gallery> getGalleriesByUser(User user) {
        return galleryRepository.findByUser(user);
    }

    // list all galleries (used on /galleries/all)
    @Transactional(readOnly = true)
    public List<Gallery> getAllGalleries() {
        return galleryRepository.findAll();
    }

    // single gallery with user + photos
    @Transactional(readOnly = true)
    public Optional<Gallery> getGalleryById(int id) {
        return galleryRepository.findById(id);
    }

    // save or update a gallery
    @Transactional
    public Gallery saveGallery(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    // delete gallery
    @Transactional
    public void deleteGallery(int id) {
        galleryRepository.deleteById(id);
    }

    // get all galleries and set a preview URL for each one
    public List<Gallery> getAllGalleriesWithPreview() {
        List<Gallery> galleries = galleryRepository.findAll();

        for (Gallery g : galleries) {

            List<Photo> photos = photoRepository.findByGallery(g);

            if (photos != null && !photos.isEmpty()) {
                // first photo in that gallery
                Photo firstPhoto = photos.get(0);
                g.setPreviewUrl(firstPhoto.getUrl());
            } else {
                // fallback image if gallery has no photos
                g.setPreviewUrl("/images/background.jpg");
            }
        }

        return galleries;
    }
}
