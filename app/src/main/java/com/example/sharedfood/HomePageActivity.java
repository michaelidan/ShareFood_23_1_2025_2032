package com.example.sharedfood;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        checkAndDeleteExpiredPosts();

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

    private void checkAndDeleteExpiredPosts() {
        // שליפת כל הפוסטים מ-Firebase Firestore
        db.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Long timestamp = document.getLong("timestamp");
                    if (timestamp != null) {
                        // חשבי את זמן התפוגה על פי הפילטרים
                        Long expirationTime = calculateExpirationTime(document);
                        if (System.currentTimeMillis() > expirationTime) {
                            // אם הפוסט פג תוקף - מחיקת הפוסט
                            db.collection("posts").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firebase", "Post deleted: " + document.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firebase", "Error deleting post", e);
                                    });
                        }
                    }
                }
            } else {
                Log.e("Firebase", "Error getting posts: ", task.getException());
            }
        });
    }

    private Long calculateExpirationTime(QueryDocumentSnapshot document) {
        Long timestamp = document.getLong("timestamp");
        if (timestamp == null) return Long.MAX_VALUE; // ללא Timestamp הפוסט לא ימחק

        List<String> filters = (List<String>) document.get("filters");
        long expirationMillis = 0;

        // אם הפוסט מכיל את הפילטרים "חם" או "קר", נחשב את זמן התפוגה
        if (filters != null && !filters.isEmpty()) {
            if (filters.contains("Hot")) {
                expirationMillis = Math.max(expirationMillis, 12 * 60 * 60 * 1000); // 12 שעות
            }
            if (filters.contains("Cold")) {
                expirationMillis = Math.max(expirationMillis, 72 * 60 * 60 * 1000); // 72 שעות
            }
        }

        // אם אין פילטרים "חם" או "קר", הפוסט לא יימחק
        if (expirationMillis == 0) {
            return Long.MAX_VALUE; // זמן תפוגה אינסופי כדי למנוע מחיקה
        }
        return timestamp + expirationMillis; // זמן התפוגה מחושב
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