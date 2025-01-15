package com.example.sharedfood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private FirebaseFirestore db;
    private TextView emptyStateText;
    private EditText cityInput;
    private List<Post> postsList;

    // פילטרים
    private CheckBox kosherCheckBox, veganCheckBox, vegetarianCheckBox, glutenFreeCheckBox,
            hotCheckBox, coldCheckBox, closedCheckBox, dairyCheckBox, meatCheckBox;

    private static final String TAG = "FeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.postsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        cityInput = findViewById(R.id.cityInput);
        postsList = new ArrayList<>();

        // Initialize filters
        kosherCheckBox = findViewById(R.id.kosherCheckBox);
        veganCheckBox = findViewById(R.id.veganCheckBox);
        vegetarianCheckBox = findViewById(R.id.vegetarianCheckBox);
        glutenFreeCheckBox = findViewById(R.id.glutenFreeCheckBox);
        hotCheckBox = findViewById(R.id.hotCheckBox);
        coldCheckBox = findViewById(R.id.coldCheckBox);
        closedCheckBox = findViewById(R.id.closedCheckBox);
        dairyCheckBox = findViewById(R.id.dairyCheckBox);
        meatCheckBox = findViewById(R.id.meatCheckBox);

        setupRecyclerView();

        // Add listeners to filters and city input
        setupFilterListeners();
        setupCityInputListener();

        loadPosts(""); // Load all posts initially
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(postsList);
        recyclerView.setAdapter(adapter);
    }

    private void setupCityInputListener() {
        cityInput.setOnEditorActionListener((v, actionId, event) -> {
            String city = cityInput.getText().toString().trim();
            loadPosts(city);
            return true;
        });
    }

    private void loadPosts(String city) {
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Post post = new Post();
                                post.setDescription(document.getString("description"));

                                // שחזור תמונה
                                String base64Image = document.getString("imageBase64");
                                if (base64Image != null) {
                                    Bitmap bitmap = decodeBase64ToBitmap(base64Image);
                                    post.setImageBitmap(bitmap);
                                }

                                post.setFilters((List<String>) document.get("filters"));
                                post.setCity(document.getString("city"));

                                // סינון לפי עיר
                                if (!city.isEmpty() && (post.getCity() == null || !post.getCity().toLowerCase().contains(city.toLowerCase()))) {
                                    continue;
                                }

                                if (isPostMatchingFilters(post)) {
                                    postsList.add(post);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing document: " + e.getMessage());
                            }
                        }

                        updateEmptyState();
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void setupFilterListeners() {
        CheckBox[] checkBoxes = {
                kosherCheckBox, veganCheckBox, vegetarianCheckBox,
                glutenFreeCheckBox, hotCheckBox, coldCheckBox,
                closedCheckBox, dairyCheckBox, meatCheckBox
        };

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> loadPosts(cityInput.getText().toString().trim()));
        }
    }

    private boolean isPostMatchingFilters(Post post) {
        if (kosherCheckBox.isChecked() && !post.hasFilter("Kosher")) return false;
        if (veganCheckBox.isChecked() && !post.hasFilter("vegan")) return false;
        if (vegetarianCheckBox.isChecked() && !post.hasFilter("vegetarian")) return false;
        if (glutenFreeCheckBox.isChecked() && !post.hasFilter("glutenFree")) return false;
        if (hotCheckBox.isChecked() && !post.hasFilter("Hot")) return false;
        if (coldCheckBox.isChecked() && !post.hasFilter("Cold")) return false;
        if (closedCheckBox.isChecked() && !post.hasFilter("Closed")) return false;
        if (dairyCheckBox.isChecked() && !post.hasFilter("Dairy")) return false;
        if (meatCheckBox.isChecked() && !post.hasFilter("Meat")) return false;

        return true;
    }

    private void updateEmptyState() {
        if (postsList.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
