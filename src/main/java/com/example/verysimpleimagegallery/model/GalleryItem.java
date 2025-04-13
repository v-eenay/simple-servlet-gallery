package com.example.verysimpleimagegallery.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GalleryItem {
    private int id;
    private String title;
    private byte[] image;
    private int userId;
    private String userName; // Added field for user's name
    private List<Tag> tags = new ArrayList<>(); // List of tags associated with this image

    public GalleryItem() {
    }

    public GalleryItem(String title, byte[] image, int userId) {
        this.title = title;
        this.image = image;
        this.userId = userId;
    }

    public GalleryItem(int id, String title, byte[] image, int userId) {
        this.id = id;
        this.title = title;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
        if (image != null) {
            return Base64.getEncoder().encodeToString(image);
        }
        return "";
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public String getTagsAsString() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(tag.getName());
        }
        return sb.toString();
    }
}
