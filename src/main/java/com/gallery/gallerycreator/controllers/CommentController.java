package com.gallery.gallerycreator.controllers;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Photo;
import com.gallery.gallerycreator.models.User;
import com.gallery.gallerycreator.services.CommentService;
import com.gallery.gallerycreator.services.PhotoService;
import com.gallery.gallerycreator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    // Post a comment on a photo
    @PostMapping("/add")
    public String addComment(@RequestParam int photoId, @RequestParam String text, Principal principal) {
        Optional<Photo> optionalPhoto = photoService.getPhotoById(photoId);
        User user = userService.getUserByUsername(principal.getName());

        if (optionalPhoto.isPresent()) {
            Comment comment = new Comment();
            comment.setPhoto(optionalPhoto.get());
            comment.setUser(user);
            comment.setText(text);
            commentService.saveComment(comment);
        }

        return "redirect:/photos/view/" + photoId;
    }
}
