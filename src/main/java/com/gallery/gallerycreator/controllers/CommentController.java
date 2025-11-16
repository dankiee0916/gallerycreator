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
@RequestMapping("/galleries")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private UserService userService;

    // handle form submit to add a new comment
    @PostMapping("/{galleryId}/comments")
    public String addComment(@PathVariable int galleryId,
                             @ModelAttribute("newComment") Comment formComment,
                             Principal principal) {

        // must be logged in to comment
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Gallery> galleryOpt = galleryService.getGalleryById(galleryId);
        if (galleryOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        Gallery gallery = galleryOpt.get();
        User user = userService.getUserByUsername(principal.getName());

        Comment comment = new Comment();
        comment.setText(formComment.getText());
        comment.setGallery(gallery);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        commentService.addComment(comment);

        return "redirect:/galleries/" + galleryId;
    }

    // delete a comment (author or gallery owner allowed)
    @PostMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable int commentId, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Comment> commentOpt = commentService.getCommentById(commentId);
        if (commentOpt.isEmpty()) {
            return "redirect:/galleries";
        }

        Comment comment = commentOpt.get();
        int galleryId = comment.getGallery().getId();

        String username = principal.getName();

        boolean isAuthor = comment.getUser() != null &&
                username.equals(comment.getUser().getUsername());

        boolean isGalleryOwner = comment.getGallery() != null &&
                comment.getGallery().getUser() != null &&
                username.equals(comment.getGallery().getUser().getUsername());

        // only delete if user wrote it or owns the gallery
        if (isAuthor || isGalleryOwner) {
            commentService.deleteComment(commentId);
        }

        return "redirect:/galleries/" + galleryId;
    }
}
