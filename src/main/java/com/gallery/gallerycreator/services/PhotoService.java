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

    // add new photo
    @Transactional
    public Photo addPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // get one photo, with gallery + user loaded
    @Transactional(readOnly = true)
    public Optional<Photo> getPhotoById(int id) {
        return photoRepository.findById(id);
    }

    // all photos in a gallery
    @Transactional(readOnly = true)
    public List<Photo> getPhotosByGallery(Gallery gallery) {
        return photoRepository.findByGallery(gallery);
    }

    // update photo
    @Transactional
    public Photo updatePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    // delete photo
    @Transactional
    public void deletePhoto(int id) {
        photoRepository.deleteById(id);
    }
}
