package com.gallery.gallerycreator.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.services.CommentService;
import com.gallery.gallerycreator.services.GalleryService;
import com.gallery.gallerycreator.services.UserService;

@Controller
@RequestMapping("/galleries")   // base path for all routes here
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private UserService userService;

    // add a new comment to a gallery
    @PostMapping("/{galleryId}/comments")
    public String addComment(@PathVariable int galleryId,
                             @ModelAttribute("newComment") Comment newComment,
                             Principal principal) {

        // make sure gallery exists
        Optional<Gallery> galleryOpt = galleryService.getGalleryById(galleryId);
        if (galleryOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        // make sure user is logged in
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByUsername(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        // fill in extra fields
        newComment.setGallery(galleryOpt.get());
        newComment.setUser(user);
        newComment.setCreatedAt(LocalDateTime.now());

        commentService.addComment(newComment);

        // send back to the gallery page
        return "redirect:/galleries/" + galleryId;
    }

    // delete a comment (owner or gallery owner only)
    @PostMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable int id, Principal principal) {

        Optional<Comment> commentOpt = commentService.getCommentById(id);
        if (commentOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        Comment comment = commentOpt.get();
        Gallery gallery = comment.getGallery();

        // no user -> just go back
        if (principal == null) {
            return "redirect:/galleries/" + gallery.getId();
        }

        String currentUser = principal.getName();
        String galleryOwner = gallery.getUser().getUsername();
        String commentOwner = comment.getUser().getUsername();

        // only delete if user owns the comment or the gallery
        if (currentUser.equals(galleryOwner) || currentUser.equals(commentOwner)) {
            commentService.deleteComment(id);
        }

        // always redirect back to the gallery page
        return "redirect:/galleries/" + gallery.getId();
    }
}
