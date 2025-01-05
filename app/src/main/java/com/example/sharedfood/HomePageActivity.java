package com.example.sharedfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Share Food Button
        findViewById(R.id.shareFoodButton).setOnClickListener(v -> {
            if (checkUserLogin()) {
                startActivity(new Intent(HomePageActivity.this, ShareYourFoodActivity.class));
            }
        });

        // Contact Us Button
        findViewById(R.id.contactUsButton).setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, activity_contact_us.class));
        });

        // Personal Area Button
        findViewById(R.id.personalAreaButton).setOnClickListener(v -> {
            if (checkUserLogin()) {
                startActivity(new Intent(HomePageActivity.this, PersonalAreaActivity.class));
            }
        });

        // Feed Button
        findViewById(R.id.feedButton).setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, FeedActivity.class));
        });

        // My Posts Button
        findViewById(R.id.myPostsButton).setOnClickListener(v -> {
            if (checkUserLogin()) {
                startActivity(new Intent(HomePageActivity.this, MyPostsActivity.class));
            }
        });

        // FAB Share Button
        ExtendedFloatingActionButton fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(v -> {
            if (checkUserLogin()) {
                startActivity(new Intent(HomePageActivity.this, ShareYourFoodActivity.class));
            }
        });
    }

    private boolean checkUserLogin() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "נא להתחבר תחילה", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
            return false;
        }
        return true;
    }
}