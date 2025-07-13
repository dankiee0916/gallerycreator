package com.gallery.gallerycreator.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Photo;

// Pulls all comments for a photo
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPhoto(Photo photo);
}
