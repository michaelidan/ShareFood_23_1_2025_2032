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

    // רכיב RecyclerView להצגת רשימת הפוסטים
    private RecyclerView postsRecyclerView;
    // אדפטר מותאם אישית להצגת הפוסטים וניהול פעולות כמו מחיקה ועריכה
    private MyPostsAdapter postsAdapter;
    // רשימה המכילה את הפוסטים שנמשכים מהמסד נתונים
    private List<Post> postList;
    // משתנה לניהול חיבור למסד הנתונים של Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard); // קביעת תצוגת המסך לפי קובץ XML

        // אתחול ה-RecyclerView והגדרת פריסת הפריטים בתצוגה אנכית
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // אתחול רשימת הפוסטים הריקה והגדרת האדפטר
        postList = new ArrayList<>();
        postsAdapter = new MyPostsAdapter(postList, this::deletePost, this::editPost);
        postsRecyclerView.setAdapter(postsAdapter);

        // אתחול החיבור למסד נתונים של Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // קריאה לפונקציה לטעינת הפוסטים מהמסד נתונים
        loadPosts();
    }

    // פונקציה לטעינת הפוסטים מהמסד נתונים
    private void loadPosts() {
        db.collection("posts") // גישה לאוסף "posts" במסד הנתונים
                .get() // קבלת כל המסמכים באוסף
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear(); // ניקוי הרשימה הקיימת
                    postList.addAll(queryDocumentSnapshots.toObjects(Post.class)); // המרת המסמכים לאובייקטים מסוג Post
                    postsAdapter.notifyDataSetChanged(); // עדכון התצוגה בנתונים החדשים
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load posts", Toast.LENGTH_SHORT).show()); // טיפול במקרה של כשל בטעינה
    }

    // פונקציה למחיקת פוסט מסוים מהמסד נתונים
    private void deletePost(Post post) {
        db.collection("posts")
                .document(post.getId()) // גישה למסמך לפי מזהה ייחודי
                .delete() // מחיקת המסמך
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show(); // הצגת הודעת הצלחה
                    loadPosts(); // טעינה מחדש של הפוסטים לאחר מחיקה
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete post", Toast.LENGTH_SHORT).show()); // טיפול במקרה של כשל במחיקה
    }

    // פונקציה לעריכת פוסט מסוים
    private void editPost(Post post) {
        Intent intent = new Intent(this, ShareYourFoodActivity.class); // יצירת Intent למעבר לפעילות עריכה
        intent.putExtra("POST_TO_EDIT", post); // העברת נתוני הפוסט לעריכה
        startActivity(intent); // הפעלת הפעילות לעריכה
    }
}

// הסבר כללי:
// קובץ זה מגדיר את פעילות מנהל המערכת של האפליקציה ShareFood.
// הפעילות כוללת הצגת רשימת פוסטים שנמשכים ממסד הנתונים Firebase Firestore.
// מנהל המערכת יכול למחוק פוסטים קיימים או לערוך אותם באמצעות פונקציות מותאמות אישית.
// התצוגה מנוהלת על ידי RecyclerView ואדפטר מותאם אישית לניהול פעולות על הפוסטים.
