package com.gallery.gallerycreator.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Gallery;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // get all comments for a gallery ordered by time
    List<Comment> findByGalleryOrderByCreatedAtAsc(Gallery gallery);
}
