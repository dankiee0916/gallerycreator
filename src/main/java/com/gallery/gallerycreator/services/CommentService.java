package com.gallery.gallerycreator.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Photo;
import com.gallery.gallerycreator.repos.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    // Get all comments for a photo
    public List<Comment> getCommentsByPhoto(Photo photo) {
        return commentRepo.findByPhoto(photo);
    }

    // Save new comment
    public void saveComment(Comment comment) {
        commentRepo.save(comment);
    }
}
