package com.example.sharedfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManagePostsActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private AdminPostsAdapter postsAdapter;
    private List<Post> postList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_posts);

        // אתחול Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // אתחול רשימת הפוסטים
        postList = new ArrayList<>();

        // אתחול RecyclerView
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // אתחול Adapter
        postsAdapter = new AdminPostsAdapter(postList, new AdminPostsAdapter.OnPostDeleteListener() {
            @Override
            public void onDeletePost(String postId) {
                deletePost(postId);
            }
        }, new AdminPostsAdapter.OnPostEditListener() {
            @Override
            public void onEditPost(Post post) {
                editPost(post);
            }
        });

        postsRecyclerView.setAdapter(postsAdapter);

        // טעינת הפוסטים
        loadPosts();
    }

    private void loadPosts() {
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    postList.addAll(queryDocumentSnapshots.toObjects(Post.class));
                    postsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load posts", Toast.LENGTH_SHORT).show());
    }

    private void deletePost(String postId) {
        db.collection("posts").document(postId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    loadPosts(); // רענון רשימת הפוסטים
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete post", Toast.LENGTH_SHORT).show());
    }

    private void editPost(Post post) {
        Intent intent = new Intent(this, ShareYourFoodActivity.class);
        intent.putExtra("POST_TO_EDIT", post);
        startActivity(intent);
    }
}
