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

    // upload form
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

        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(galleryId);
        if (optionalGallery.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery gallery = optionalGallery.get();
        if (gallery.getUser() == null
                || !gallery.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        // simple local upload – same as you had
        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destinationFile = new File(uploadPath, fileName);
            file.transferTo(destinationFile);
            photo.setUrl("/uploads/" + fileName);
        }

        photo.setGallery(gallery);
        photoService.addPhoto(photo);

        return "redirect:/galleries/" + galleryId;
    }

    // edit form
    @GetMapping("/edit/{photoId}")
    public String showEditForm(@PathVariable int photoId, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isEmpty()) {
            return "redirect:/galleries";
        }

        Photo photo = optionalPhoto.get();
        if (photo.getGallery().getUser() == null
                || !photo.getGallery().getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        model.addAttribute("photo", photo);
        model.addAttribute("galleryId", photo.getGallery().getId());
        return "editphoto";
    }

    // handle edit submit
    @PostMapping("/edit")
    public String editPhoto(@ModelAttribute Photo form,
            @RequestParam("file") MultipartFile file,
            @RequestParam("galleryId") int galleryId,
            Principal principal) throws IOException {

        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Photo> optionalPhoto = photoService.getPhotoById(form.getId());
        if (optionalPhoto.isEmpty()) {
            return "redirect:/galleries";
        }

        Photo existing = optionalPhoto.get();
        Gallery gallery = existing.getGallery();

        if (gallery.getUser() == null
                || !gallery.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        existing.setCaption(form.getCaption());

        // replace file only if a new one is uploaded
        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destinationFile = new File(uploadPath, fileName);
            file.transferTo(destinationFile);
            existing.setUrl("/uploads/" + fileName);
        }

        photoService.updatePhoto(existing);
        return "redirect:/galleries/" + galleryId;
    }

    // delete photo (GET is fine for your class)
    @GetMapping("/delete/{photoId}")
    public String deletePhoto(@PathVariable int photoId, Principal principal) {
        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isEmpty()) {
            return "redirect:/galleries";
        }

        Photo photo = optionalPhoto.get();

        // grab gallery and owner info up front
        Gallery gallery = photo.getGallery();
        int galleryId = (gallery != null) ? gallery.getId() : 0;
        String ownerUsername = (gallery != null && gallery.getUser() != null)
                ? gallery.getUser().getUsername()
                : null;

        // only allow delete if logged in user is owner
        if (principal != null && ownerUsername != null
                && principal.getName().equals(ownerUsername)) {
            photoService.deletePhoto(photoId);
        }

        if (galleryId > 0) {
            return "redirect:/galleries/" + galleryId;
        } else {
            return "redirect:/galleries";
        }
    }

    @GetMapping("/view/{id}")
    public String viewPhoto(@PathVariable("id") int id, Model model) {

        // get the optional photo from the service
        Optional<Photo> optionalPhoto = photoService.getPhotoById(id);

        // if not found, just go back to all galleries
        if (optionalPhoto.isEmpty()) {
            return "redirect:/galleries/all";
        }

        // unwrap the Optional so Thymeleaf gets a real Photo object
        Photo photo = optionalPhoto.get();

        model.addAttribute("photo", photo);

        return "viewphoto";
    }

}
