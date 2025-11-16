package com.gallery.gallerycreator.repos;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gallery.gallerycreator.models.Comment;
import com.gallery.gallerycreator.models.Gallery;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Load the user and gallery along with each comment
    @EntityGraph(attributePaths = { "user", "gallery" })
    List<Comment> findByGalleryOrderByCreatedAtAsc(Gallery gallery);
}
