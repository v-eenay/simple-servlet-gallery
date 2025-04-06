package com.example.verysimpleimagegallery.model;

import java.sql.Timestamp;

public class GalleryItem {
    private int id;
    private String title;
    private byte[] imageData;
    private int userId;
    private String userName;  // To store the name of the image uploader
    private String base64Image;  // For displaying images in UI
    private Timestamp createdAt;

    public GalleryItem() {
    }

    public GalleryItem(String title, byte[] imageData, int userId) {
        this.title = title;
        this.imageData = imageData;
        this.userId = userId;
    }

    public GalleryItem(int id, String title, byte[] imageData, int userId) {
        this.id = id;
        this.title = title;
        this.imageData = imageData;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
