package com.gallery.gallerycreator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.Photo;
import com.gallery.gallerycreator.repos.PhotoRepository;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    // Add a new photo
    @Transactional
    public Photo addPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // Get all photos
    @Transactional(readOnly = true)
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    // Get photo by ID
    @Transactional(readOnly = true)
    public Optional<Photo> getPhotoById(int id) {
        return photoRepository.findById(id);
    }

    // Get all photos in a specific gallery (entity version)
    @Transactional(readOnly = true)
    public List<Photo> getPhotosByGallery(Gallery gallery) {
        return photoRepository.findByGallery(gallery);
    }

    // Get all photos in a specific gallery (id version – use this from controllers)
    @Transactional(readOnly = true)
    public List<Photo> getPhotosByGalleryId(int galleryId) {
        return photoRepository.findByGallery_Id(galleryId);
    }

    // Update photo info
    @Transactional
    public Photo updatePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // Delete photo by ID
    @Transactional
    public void deletePhoto(int id) {
        photoRepository.deleteById(id);
    }
}
