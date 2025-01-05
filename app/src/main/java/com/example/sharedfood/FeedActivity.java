package com.example.sharedfood;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout postsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize Firestore and UI elements
        db = FirebaseFirestore.getInstance();
        postsContainer = findViewById(R.id.postsContainer);

        // Load posts from Firestore
        loadPostsFromFirestore();
    }

    private void loadPostsFromFirestore() {
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Post post = document.toObject(Post.class);
                            posts.add(post);
                        } catch (Exception e) {
                            Log.e("FeedActivity", "Error converting document to Post", e);
                        }
                    }
                    displayPosts(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e("FeedActivity", "Failed to load posts", e);
                    Toast.makeText(this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                });
    }

    private void displayPosts(List<Post> posts) {
        if (posts.isEmpty()) {
            Toast.makeText(this, "No posts available", Toast.LENGTH_SHORT).show();
            return;
        }

        postsContainer.removeAllViews(); // Clear previous views

        for (Post post : posts) {
            // Create a layout for each post
            LinearLayout postLayout = new LinearLayout(this);
            postLayout.setOrientation(LinearLayout.VERTICAL);
            postLayout.setPadding(16, 16, 16, 16);

            // Add description
            if (post.getDescription() != null) {
                TextView descriptionView = new TextView(this);
                descriptionView.setText(post.getDescription());
                postLayout.addView(descriptionView);
            }

            // Add image if available or placeholder if not
            if (post.getImageUri() != null ) {
                ImageView imageView = new ImageView(this);
                Glide.with(this).load(post.getImageUri()).into(imageView);
                postLayout.addView(imageView);
            } else {
                // Add a placeholder image if imageUrl is null or empty
                ImageView placeholder = new ImageView(this);
                placeholder.setImageResource(R.drawable.placeholder_image); // Replace with your placeholder image
                postLayout.addView(placeholder);
            }

            // Add the post layout to the container
            postsContainer.addView(postLayout);
        }
    }
}
