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

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.services.CommentService;
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

    @Autowired
    private CommentService commentService;

    // list galleries for logged in user
    @GetMapping
    public String listGalleries(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User me = userService.getUserByUsername(principal.getName());
        List<Gallery> mine = galleryService.getGalleriesByUser(me);
        model.addAttribute("galleries", mine);
        model.addAttribute("isMyGallery", true);
        return "galleries";
    }

    // show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("gallery", new Gallery());
        return "creategallery";
    }

    // handle create submit
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

    // edit form
    @GetMapping("/edit/{id}")
    public String editGallery(@PathVariable int id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> opt = galleryService.getGalleryById(id);
        if (opt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery g = opt.get();
        if (g.getUser() == null
                || !g.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        model.addAttribute("gallery", g);
        return "editgallery";
    }

    // handle edit submit
    @PostMapping("/edit")
    public String updateGallery(@ModelAttribute Gallery form, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> existingOpt = galleryService.getGalleryById(form.getId());
        if (existingOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery existing = existingOpt.get();
        if (existing.getUser() == null
                || !existing.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/galleries";
        }

        existing.setTitle(form.getTitle());
        existing.setDescription(form.getDescription());
        galleryService.saveGallery(existing);

        return "redirect:/galleries/" + existing.getId();
    }

    // delete gallery
    @PostMapping("/delete/{id}")
    public String deleteGallery(@PathVariable int id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> opt = galleryService.getGalleryById(id);
        if (opt.isPresent()
                && opt.get().getUser() != null
                && opt.get().getUser().getUsername().equals(principal.getName())) {
            galleryService.deleteGallery(id);
        }

        return "redirect:/galleries";
    }

    // view single gallery + photos + comments
    @GetMapping("/{id}")
    public String viewGallery(@PathVariable int id, Model model, Principal principal) {

        // this method should load user and photos (you already had it in the service)
        Optional<Gallery> opt = galleryService.getGalleryById(id);
        if (opt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery g = opt.get();
        model.addAttribute("gallery", g);

        boolean ownsGallery = principal != null
                && g.getUser() != null
                && g.getUser().getUsername().equals(principal.getName());
        model.addAttribute("ownsGallery", ownsGallery);

        // load comments for this gallery
        List<Comment> comments = commentService.getCommentsForGallery(g);
        model.addAttribute("comments", comments);

        // object for the new comment form
        model.addAttribute("newComment", new Comment());

        return "gallery";
    }

    // show all galleries with preview images (public page)
    @GetMapping("/all")
    public String viewAllGalleries(Model model) {
        List<Gallery> galleries = galleryService.getAllGalleriesWithPreview();

        model.addAttribute("galleries", galleries);
        model.addAttribute("isMyGallery", false);

        return "gallerylist";
    }

}
