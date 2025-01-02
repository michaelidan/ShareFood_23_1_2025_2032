package com.example.sharedfood;

import android.net.Uri;

import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import com.google.firebase.firestore.GeoPoint;
import android.content.Context;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Locale;

public class Post {
    // הגדרת השדות כ- final כדי למנוע מהם שינוי אחרי יצירת האובייקט
    private String userId;
    private String description;
    private List<String> filters;
    private String imageUrl; // URL של התמונה (יכול להשתנות אחרי יצירת האובייקט)
    private Uri imageUri; // URI של התמונה
    private String city;
    private GeoPoint location; // מיקום המשתמש, מסוג GeoPoint

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
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
    // הוספת גטרים וסטטרים לשדה העיר
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}