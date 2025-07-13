package com.gallery.gallerycreator.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    // Each comment belongs to a photo
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;


    public Comment() {}

    public Comment(String text, Photo photo, User user) {
    this.text = text;
    this.photo = photo;
}

    // Getters and setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public Photo getPhoto() { return photo; }

    public void setPhoto(Photo photo) { this.photo = photo; }

    public void setUser(User user) {
}
}