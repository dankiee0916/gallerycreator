package com.gallery.gallerycreator.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gallery.gallerycreator.models.Photo;
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.ImageUploadService;
import com.gallery.gallerycreator.services.PhotoService;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private GalleryService galleryService;

     @Autowired
    private ImageUploadService imageUploadService; // cloud upload

    // upload page
    @GetMapping("/upload/{galleryId}")
    public String showUploadForm(@PathVariable int galleryId, Model model) {
        model.addAttribute("photo", new Photo());
        model.addAttribute("galleryId", galleryId);
        return "uploadphoto";
    }

    // handle upload
    @PostMapping("/upload")
    public String uploadPhoto(@ModelAttribute Photo photo,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("galleryId") int galleryId,
                              Principal principal) throws IOException {

        var optionalGallery = galleryService.getGalleryById(galleryId);
        if (optionalGallery.isPresent()) {
            var gallery = optionalGallery.get();

            // only owner can add photos
            if (gallery.getUser().getUsername().equals(principal.getName())) {

                // if they picked a file, push to Cloudinary and save returned https url
                if (file != null && !file.isEmpty()) {
                    String httpsUrl = imageUploadService.upload(file);
                    photo.setUrl(httpsUrl); // store external url (no /uploads local path)
                }

                photo.setGallery(gallery);
                photoService.addPhoto(photo);
            }
        }
        return "redirect:/galleries/" + galleryId;
    }

    // edit page
    @GetMapping("/edit/{photoId}")
    public String showEditForm(@PathVariable int photoId, Model model, Principal principal) {
        var optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isPresent()) {
            var photo = optionalPhoto.get();
            if (photo.getGallery().getUser().getUsername().equals(principal.getName())) {
                model.addAttribute("photo", photo);
                model.addAttribute("galleryId", photo.getGallery().getId());
                return "editphoto";
            }
        }
        return "redirect:/galleries";
    }

    // edit submit (keep old url if no new file)
    @PostMapping("/edit")
    public String editPhoto(@ModelAttribute Photo form,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("galleryId") int galleryId,
                            Principal principal) throws IOException {

        var optionalGallery = galleryService.getGalleryById(galleryId);
        if (optionalGallery.isPresent()) {
            var gallery = optionalGallery.get();

            if (gallery.getUser().getUsername().equals(principal.getName())) {
                var existingOpt = photoService.getPhotoById(form.getId());
                if (existingOpt.isPresent()) {
                    var existing = existingOpt.get();

                    // if a new file was uploaded, replace the url with a fresh upload
                    if (file != null && !file.isEmpty()) {
                        String httpsUrl = imageUploadService.upload(file);
                        existing.setUrl(httpsUrl);
                    }

                    existing.setCaption(form.getCaption());
                    existing.setGallery(gallery);
                    photoService.updatePhoto(existing);
                }
            }
        }
        return "redirect:/galleries/" + galleryId;
    }

    @GetMapping("/delete/{photoId}")
    public String deletePhoto(@PathVariable int photoId, Principal principal) {
        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            if (photo.getGallery().getUser().getUsername().equals(principal.getName())) {
                int galleryId = photo.getGallery().getId();
                photoService.deletePhoto(photoId);
                return "redirect:/galleries/" + galleryId;
            }
        }
        return "redirect:/galleries";
    }

    @GetMapping("/view/{photoId}")
    public String viewPhoto(@PathVariable int photoId, Model model) {
        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isPresent()) {
            model.addAttribute("photo", optionalPhoto.get());
            return "viewphoto";
        }
        return "redirect:/galleries";
    }
}
