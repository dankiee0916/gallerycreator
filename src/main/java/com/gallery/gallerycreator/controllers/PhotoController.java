package com.gallery.gallerycreator.controllers;

import java.io.File;
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

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.Photo;
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.PhotoService;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private GalleryService galleryService;

    @GetMapping("/upload/{galleryId}")
    public String showUploadForm(@PathVariable int galleryId, Model model) {
        model.addAttribute("photo", new Photo());
        model.addAttribute("galleryId", galleryId);
        return "upload-photo";
    }

    @PostMapping("/upload")
    public String uploadPhoto(@ModelAttribute Photo photo,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("galleryId") int galleryId,
                              Principal principal) throws IOException {

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(galleryId);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            if (gallery.getUser().getUsername().equals(principal.getName())) {
                if (!file.isEmpty()) {
                    String uploadDir = "src/main/resources/static/uploads/";
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    File destinationFile = new File(uploadDir + fileName);
                    destinationFile.getParentFile().mkdirs();
                    file.transferTo(destinationFile);

                    photo.setUrl("/uploads/" + fileName);
                }

                photo.setGallery(gallery);
                photoService.addPhoto(photo);
            }
        }

        return "redirect:/galleries/" + galleryId;
    }

    @GetMapping("/edit/{photoId}")
    public String showEditForm(@PathVariable int photoId, Model model, Principal principal) {
        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            if (photo.getGallery().getUser().getUsername().equals(principal.getName())) {
                model.addAttribute("photo", photo);
                model.addAttribute("galleryId", photo.getGallery().getId());
                return "edit-photo";
            }
        }
        return "redirect:/galleries";
    }

    @PostMapping("/edit")
    public String editPhoto(@ModelAttribute Photo photo,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("galleryId") int galleryId,
                            Principal principal) throws IOException {

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(galleryId);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            if (gallery.getUser().getUsername().equals(principal.getName())) {
                if (!file.isEmpty()) {
                    String uploadDir = "src/main/resources/static/uploads/";
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    File destinationFile = new File(uploadDir + fileName);
                    destinationFile.getParentFile().mkdirs();
                    file.transferTo(destinationFile);
                    photo.setUrl("/uploads/" + fileName);
                }

                photo.setGallery(gallery);
                photoService.updatePhoto(photo);
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
}
