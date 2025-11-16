package com.gallery.gallerycreator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Gallery;
import com.gallery.gallerycreator.repos.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // save a new comment
    @Transactional
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // get all comments for one gallery
    @Transactional(readOnly = true)
    public List<Comment> getCommentsForGallery(Gallery gallery) {
        return commentRepository.findByGalleryOrderByCreatedAtAsc(gallery);
    }

    // find a single comment
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(int id) {
        return commentRepository.findById(id);
    }

    // delete a comment by id
    @Transactional
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }
}
