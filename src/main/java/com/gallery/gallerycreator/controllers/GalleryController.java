package com.gallery.gallerycreator.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.PhotoService;

@Controller
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PhotoService photoService;

// View a single gallery
    @GetMapping("/{id}")
    public String viewGallery(@PathVariable int id, Model model, Principal principal) {
        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            model.addAttribute("gallery", gallery);

            // who owns this gallery?
            boolean ownsGallery = false;
            if (principal != null) {
                ownsGallery = gallery.getUser() != null
                        && gallery.getUser().getUsername().equals(principal.getName());
            }
            model.addAttribute("ownsGallery", ownsGallery);

            // avoid lazy issues by loading photos explicitly
            model.addAttribute("photos", photoService.getPhotosByGallery(gallery));

            return "gallery";
        }
        return "redirect:/galleries";
    }

// Show edit form (only if the logged-in user owns the gallery)
    @GetMapping("/edit/{id}")
    public String editGallery(@PathVariable int id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()
                && optionalGallery.get().getUser() != null
                && optionalGallery.get().getUser().getUsername().equals(principal.getName())) {
            model.addAttribute("gallery", optionalGallery.get());
            return "editgallery";
        }
        return "redirect:/galleries";
    }

// Save gallery updates (only if the user owns it)
    @PostMapping("/edit")
    public String updateGallery(@ModelAttribute Gallery form, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // load the real entity first (form.user is null)
        Optional<Gallery> existingOpt = galleryService.getGalleryById(form.getId());
        if (existingOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery existing = existingOpt.get();
        if (existing.getUser() == null
                || !existing.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        // update allowed fields
        existing.setTitle(form.getTitle());
        existing.setDescription(form.getDescription());
        galleryService.saveGallery(existing);

        return "redirect:/galleries/" + existing.getId();
    }

// Delete a gallery (POST is safer)
    @PostMapping("/delete/{id}")
    public String deleteGallery(@PathVariable int id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()
                && optionalGallery.get().getUser() != null
                && optionalGallery.get().getUser().getUsername().equals(principal.getName())) {
            galleryService.deleteGallery(id);
        }
        return "redirect:/galleries";
    }
}
