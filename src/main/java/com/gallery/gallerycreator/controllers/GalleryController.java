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
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.PhotoService;
import com.gallery.gallerycreator.services.UserService;

@Controller
@RequestMapping("/galleries")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    // list galleries for the logged-in user
    @GetMapping
    public String listGalleries(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User me = userService.getUserByUsername(principal.getName());
        java.util.List<Gallery> mine = galleryService.getGalleriesByUser(me);
        model.addAttribute("galleries", mine);
        model.addAttribute("isMyGallery", true);          // show “Create” and owner buttons
        model.addAttribute("showOwnerButtons", true);     // edit/delete visible
        return "galleries";
    }

    // show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("gallery", new Gallery());
        return "creategallery";
    }

    // create submit
    @PostMapping("/create")
    public String createGallery(@ModelAttribute Gallery form, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User me = userService.getUserByUsername(principal.getName());
        form.setUser(me);
        galleryService.saveGallery(form);
        return "redirect:/galleries";
    }

    // edit form (need owner fetched to check ownership)
    @GetMapping("/edit/{id}")
    public String editGallery(@PathVariable int id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> opt = galleryService.getGalleryById(id); // this fetches user eagerly
        if (opt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery g = opt.get();
        if (g.getUser() == null || !g.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        model.addAttribute("gallery", g);
        return "editgallery";
    }

    // edit submit
    @PostMapping("/edit")
    public String updateGallery(@ModelAttribute Gallery form, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> existingOpt = galleryService.getGalleryById(form.getId()); // user fetched
        if (existingOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery existing = existingOpt.get();
        if (existing.getUser() == null || !existing.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        // only update editable fields
        existing.setTitle(form.getTitle());
        existing.setDescription(form.getDescription());
        galleryService.saveGallery(existing);

        return "redirect:/galleries/" + existing.getId();
    }

    // delete (POST is safer, keep as-is if you wired the CSRF token)
    @PostMapping("/delete/{id}")
    public String deleteGallery(@PathVariable int id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> opt = galleryService.getGalleryById(id); // user fetched
        if (opt.isPresent() && opt.get().getUser() != null
                && opt.get().getUser().getUsername().equals(principal.getName())) {
            galleryService.deleteGallery(id);
        }
        return "redirect:/galleries";
    }

    // view a single gallery
    @GetMapping("/{id}")
    public String viewGallery(@PathVariable int id, Model model, Principal principal) {
        // pull owner + photos in one go so the template can read both safely
        Optional<Gallery> opt = galleryService.getGalleryWithUserAndPhotos(id);
        if (opt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery g = opt.get();
        model.addAttribute("gallery", g);

        // who owns this
        boolean owns = principal != null
                && g.getUser() != null
                && g.getUser().getUsername().equals(principal.getName());
        model.addAttribute("ownsGallery", owns);

        model.addAttribute("photos", photoService.getPhotosByGalleryId(g.getId()));

        System.out.println("Gallery " + g.getId() + " photos count=" + ((List<?>) model.getAttribute("photos")).size());

        return "gallery";
    }

    // ALL galleries (public)
    @GetMapping("/all")
    public String allGalleries(Model model) {
        model.addAttribute("galleries", galleryService.getAllGalleries());
        model.addAttribute("isMyGallery", false);         // no “Create” button
        model.addAttribute("showOwnerButtons", false);    // hide edit/delete here
        return "galleries";
    }
}
