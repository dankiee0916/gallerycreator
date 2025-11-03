package com.gallery.gallerycreator.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 200)
    private String caption;

    @Column(nullable = false, length = 255)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Gallery getGallery() { return gallery; }
    public void setGallery(Gallery gallery) { this.gallery = gallery; }
}
