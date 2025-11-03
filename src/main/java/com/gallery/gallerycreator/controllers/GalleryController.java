package com.gallery.gallerycreator.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.repos.GalleryRepository;
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.UserService;

@Controller
@RequestMapping("/galleries")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private UserService userService;

    @Autowired
    private GalleryRepository galleryRepository;

    // Display galleries that belong to the logged-in user
    @GetMapping
    public String listGalleries(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<Gallery> userGalleries = galleryService.getGalleriesByUser(user);
        model.addAttribute("galleries", userGalleries);
        model.addAttribute("isMyGallery", true);
        return "galleries";
    }

    // Show form to create a new gallery
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("gallery", new Gallery());
        return "creategallery";
    }

    // Handle form submission to save a new gallery
    @PostMapping("/create")
    public String createGallery(@ModelAttribute Gallery gallery, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        gallery.setUser(user);
        galleryService.saveGallery(gallery);
        return "redirect:/galleries";
    }

    // Show edit form (only if the logged-in user owns the gallery)
    @GetMapping("/edit/{id}")
    public String editGallery(@PathVariable int id, Model model, Principal principal) {
        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent() &&
            optionalGallery.get().getUser().getUsername().equals(principal.getName())) {
            model.addAttribute("gallery", optionalGallery.get());
            return "editgallery";
        }
        return "redirect:/galleries";
    }

    // Save gallery updates (only if the user owns it)
    @PostMapping("/edit")
    public String updateGallery(@ModelAttribute Gallery gallery, Principal principal) {
        if (gallery.getUser().getUsername().equals(principal.getName())) {
            galleryService.saveGallery(gallery);
        }
        return "redirect:/galleries";
    }

    // Delete a gallery (only if the logged-in user owns it)
    @GetMapping("/delete/{id}")
    public String deleteGallery(@PathVariable int id, Principal principal) {
        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent() &&
            optionalGallery.get().getUser().getUsername().equals(principal.getName())) {
            galleryService.deleteGallery(id);
        }
        return "redirect:/galleries";
    }

    // View a single gallery
    @GetMapping("/{id}")
    public String viewGallery(@PathVariable int id, Model model, Principal principal) {
        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            model.addAttribute("gallery", gallery);

            boolean ownsGallery = false;
            if (principal != null) {
                ownsGallery = gallery.getUser().getUsername().equals(principal.getName());
            }
            model.addAttribute("ownsGallery", ownsGallery);

            return "gallery";
        }
        return "redirect:/galleries";
    }

    // Show all galleries
    @GetMapping("/all")
    public String viewAllGalleries(Model model) {
        List<Gallery> allGalleries = galleryRepository.findAll();
        model.addAttribute("galleries", allGalleries);
        model.addAttribute("isMyGallery", false);
        return "gallerylist";
    }
}
