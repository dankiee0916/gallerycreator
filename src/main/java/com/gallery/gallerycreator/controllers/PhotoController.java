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

    // show upload form
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

        // only owner can upload to this gallery
        if (gallery.getUser() == null ||
            !gallery.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

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

        if (photo.getGallery() == null ||
            photo.getGallery().getUser() == null ||
            !photo.getGallery().getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        model.addAttribute("photo", photo);
        model.addAttribute("galleryId", photo.getGallery().getId());
        return "editphoto";
    }

    // edit submit
    @PostMapping("/edit")
    public String editPhoto(@ModelAttribute Photo photo,
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

        if (gallery.getUser() == null ||
            !gallery.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        // load the existing photo so we don't lose fields
        Optional<Photo> existingOpt = photoService.getPhotoById(photo.getId());
        if (existingOpt.isEmpty()) {
            return "redirect:/galleries/" + galleryId;
        }

        Photo existing = existingOpt.get();

        // update caption
        existing.setCaption(photo.getCaption());

        // if new file was uploaded, replace image
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

    // delete photo
    @GetMapping("/delete/{photoId}")
    public String deletePhoto(@PathVariable int photoId, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        if (optionalPhoto.isEmpty()) {
            return "redirect:/galleries";
        }

        Photo photo = optionalPhoto.get();

        if (photo.getGallery() == null ||
            photo.getGallery().getUser() == null ||
            !photo.getGallery().getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        int galleryId = photo.getGallery().getId();

        // optional: also delete file from disk later if you want
        photoService.deletePhoto(photoId);

        return "redirect:/galleries/" + galleryId;
    }

    // view a single photo (if you still use this)
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
