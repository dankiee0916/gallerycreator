package com.gallery.gallerycreator.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.PhotoService;
import com.gallery.gallerycreator.services.UserService;

@Controller
@RequestMapping("/galleries") // base path for everything in here
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    // list my galleries (after login we land here)
    @GetMapping
    public String listGalleries(Model model, Principal principal) {
        // grab username safely (Principal can be null on first redirect)
        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                username = auth.getName();
            }
        }
        if (username == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByUsername(username);
        List<Gallery> userGalleries = galleryService.getGalleriesByUser(user);

        model.addAttribute("galleries", userGalleries);
        model.addAttribute("isMyGallery", true);
        return "galleries";
    }

    // show one gallery
    @GetMapping("/{id}")
    public String viewGallery(@PathVariable int id, Model model, Principal principal) {
        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            model.addAttribute("gallery", gallery);

            // do I own it?
            boolean ownsGallery = false;
            if (principal != null && gallery.getUser() != null) {
                ownsGallery = gallery.getUser().getUsername().equals(principal.getName());
            }
            model.addAttribute("ownsGallery", ownsGallery);

            // avoid lazy issues by loading photos explicitly
            model.addAttribute("photos", photoService.getPhotosByGallery(gallery));

            return "gallery";
        }
        return "redirect:/galleries";
    }

    // show edit form
    @GetMapping("/edit/{id}")
    public String editGallery(@PathVariable int id, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()
                && optionalGallery.get().getUser() != null
                && optionalGallery.get().getUser().getUsername().equals(principal.getName())) {
            model.addAttribute("gallery", optionalGallery.get());
            return "editgallery";
        }
        return "redirect:/galleries";
    }

    // save edits
    @PostMapping("/edit")
    public String updateGallery(@ModelAttribute Gallery form, Principal principal) {
        if (principal == null) return "redirect:/login";

        Optional<Gallery> existingOpt = galleryService.getGalleryById(form.getId());
        if (existingOpt.isEmpty()) return "redirect:/galleries";

        Gallery existing = existingOpt.get();
        if (existing.getUser() == null
                || !existing.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        // update fields we allow
        existing.setTitle(form.getTitle());
        existing.setDescription(form.getDescription());
        galleryService.saveGallery(existing);

        return "redirect:/galleries/" + existing.getId();
    }

    // delete (POST is safer)
    @PostMapping("/delete/{id}")
    public String deleteGallery(@PathVariable int id, Principal principal) {
        if (principal == null) return "redirect:/login";

        Optional<Gallery> optionalGallery = galleryService.getGalleryById(id);
        if (optionalGallery.isPresent()
                && optionalGallery.get().getUser() != null
                && optionalGallery.get().getUser().getUsername().equals(principal.getName())) {
            galleryService.deleteGallery(id);
        }
        return "redirect:/galleries";
    }

    // create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("gallery", new Gallery());
        return "creategallery";
    }

    // create submit
    @PostMapping("/create")
    public String createGallery(@ModelAttribute Gallery gallery, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userService.getUserByUsername(principal.getName());
        gallery.setUser(user);
        galleryService.saveGallery(gallery);
        return "redirect:/galleries";
    }
}
