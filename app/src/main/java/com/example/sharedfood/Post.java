package com.example.sharedfood;

import android.net.Uri;

import java.util.List;

public class Post {
    // הגדרת השדות כ- final כדי למנוע מהם שינוי אחרי יצירת האובייקט
    private String userId;
    private String description;
    private List<String> filters;
    private String imageUrl; // URL של התמונה (יכול להשתנות אחרי יצירת האובייקט)
    private Uri imageUri; // URI של התמונה


    // Constructor
    public Post(String description, List<String> filters, String imageUrl) {
        this.description = description;
        this.filters = filters;
        this.imageUrl = imageUrl;
    }
    // Empty constructor for Firebase
    public Post() {}

    // Getters and setters
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;

    }
}