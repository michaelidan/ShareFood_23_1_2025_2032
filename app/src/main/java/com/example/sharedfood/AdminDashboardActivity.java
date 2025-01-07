package com.example.sharedfood;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private AdminPostsAdapter postsAdapter;
    private List<Post> postList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postsAdapter = new AdminPostsAdapter(postList, this::deletePost);
        postsRecyclerView.setAdapter(postsAdapter);

        db = FirebaseFirestore.getInstance();

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
                    loadPosts();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete post", Toast.LENGTH_SHORT).show());
    }
}
