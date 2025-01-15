package com.example.sharedfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private MyPostsAdapter postsAdapter; // replace: AdminPostAdapter -> MyPostsAdapter
    private List<Post> postList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// Michael, 8/01/2025, START ########################
// קיים כבר: יצירת רשימת פוסטים ואדפטר
        postList = new ArrayList<>();
        postsAdapter = new MyPostsAdapter(postList, this::deletePost, this::editPost); // הוספת האופציה לעריכה  // replace: AdminPostAdapter -> MyPostsAdapter
        postsRecyclerView.setAdapter(postsAdapter);
// Michael, 8/01/2025, END ########################

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

    // Michael, 14/01/2025, START $$$$$$$$$$$$$$$$$$$$$$
    private void deletePost(Post post) {
        db.collection("posts")
                .document(post.getId()) // מחיקת המסמך לפי מזהה ייחודי
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                    loadPosts();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete post", Toast.LENGTH_SHORT).show());
    }

    // הוספת פונקציה לעריכת פוסט
    private void editPost(Post post) {   // (this changed just on 8/1/2025)
        Intent intent = new Intent(this, ShareYourFoodActivity.class);
        intent.putExtra("POST_TO_EDIT", post);
        startActivity(intent);
    }
// Michael, 14/01/2025, END ########################
} // +2
