package com.example.verysimpleimagegallery.model;

import java.sql.Timestamp;

/**
 * Model class representing a tag in the system.
 */
public class Tag {
    private int id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public Tag() {
    }
    
    // Constructor with name
    public Tag(String name) {
        this.name = name;
    }
    
    // Constructor with id and name
    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Full constructor
    public Tag(int id, String name, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Tag tag = (Tag) o;
        
        if (id != 0 && id == tag.id) return true;
        return name != null ? name.equalsIgnoreCase(tag.name) : tag.name == null;
    }
    
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.toLowerCase().hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
